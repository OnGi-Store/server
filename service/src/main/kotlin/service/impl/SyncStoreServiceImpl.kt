package service.impl

import Banner
import Menu
import Point
import RemoteStore
import Store
import StoreDetail
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import repository.local.BannerRepository
import repository.local.MenuRepository
import repository.local.StoreDetailRepository
import repository.local.StoreRepository
import repository.remote.RemoteAPIStoreRepository
import repository.remote.RemoteGeocoderRepository
import repository.remote.RemoteOfficialStoreRepository
import repository.remote.RemoteStoreBannerRepository
import service.SyncStoreService
import service.mapper.StoreMapper.toStore
import service.mapper.StoreMapper.toStoreDetail
import service.mapper.StoreMapper.toStoreMenuList
import service.util.TransactionUtil
import sync.StoreSyncResult
import sync.SyncResult
import java.util.*

internal class SyncStoreServiceImpl(
    private val bannerRepository: BannerRepository,
    private val storeRepository: StoreRepository,
    private val storeDetailRepository: StoreDetailRepository,
    private val menuRepository: MenuRepository,
    private val remoteStoreBannerRepository: RemoteStoreBannerRepository,
    private val remoteOfficialStoreRepository: RemoteOfficialStoreRepository,
    private val remoteAPIStoreRepository: RemoteAPIStoreRepository,
    private val remoteGeocoderRepository: RemoteGeocoderRepository
) : SyncStoreService {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override suspend fun syncBanner(): Int {
        val remoteBannerList: List<Banner> = remoteStoreBannerRepository.getStoreBannerList()
        bannerRepository.deleteAll()
        bannerRepository.createAll(bannerList = remoteBannerList)
        return remoteBannerList.size
    }

    override suspend fun syncStoreData(): StoreSyncResult = coroutineScope {
        val officialStoreList: Deferred<List<RemoteStore>> =
            async { remoteOfficialStoreRepository.getStoreListFromOfficial() }
        val cloudStoreList: Deferred<List<RemoteStore>> =
            async { remoteAPIStoreRepository.getStoreListFromAPI() }

        combineStore(
            officialList = officialStoreList.await(),
            cloudList = cloudStoreList.await()
        ).asFlow()
            .buffer(capacity = DEFAULT_BUFFER)
            .map { remoteStore: RemoteStore -> handleStore(remoteStore = remoteStore) }
            .fold(initial = StoreSyncResult.default()) { acc: StoreSyncResult, result: SyncResult ->
                when (result) {
                    SyncResult.CREATE_SUCCESS -> acc.copy(createdCount = acc.createdCount + 1)
                    SyncResult.CREATE_FAILED -> acc.copy(createFailCount = acc.createFailCount + 1)
                    SyncResult.UPDATE_SUCCESS -> acc.copy(updatedCount = acc.updatedCount + 1)
                    SyncResult.UPDATE_FAILED -> acc.copy(updateFailCount = acc.updateFailCount + 1)
                }
            }
    }

    private suspend fun handleStore(remoteStore: RemoteStore): SyncResult {
        val savedStore: Store? = storeRepository.findByNameAndAddress(
            name = remoteStore.name,
            address = remoteStore.address
        )
        return savedStore?.let { updateStore(store = savedStore, remoteStore = remoteStore) }
            ?: saveStore(remoteStore = remoteStore)
    }

    private suspend fun saveStore(remoteStore: RemoteStore): SyncResult = TransactionUtil.suspendedTransaction {
        runCatching {
            val point: Point = remoteGeocoderRepository.getStoreLocation(address = remoteStore.address)
            val store: Store = remoteStore.toStore(point = point)
            val storeId: UUID = storeRepository.create(store = store).id

            val storeDetail: StoreDetail = remoteStore.toStoreDetail(storeId = storeId)
            val menuList: List<Menu> = remoteStore.toStoreMenuList(storeId = storeId)
            storeDetailRepository.create(storeDetail = storeDetail)
            menuRepository.createAll(menuList = menuList)

            SyncResult.CREATE_SUCCESS
        }.onFailure { error ->
            log.error("매장 저장 실패 - 이름: ${remoteStore.name}, 주소: ${remoteStore.address}, 원인: ${error.message}")
        }.getOrElse {
            SyncResult.CREATE_FAILED
        }
    }

    private suspend fun updateStore(
        store: Store,
        remoteStore: RemoteStore
    ): SyncResult = TransactionUtil.suspendedTransaction {
        runCatching {
            val updatedStore: Store = store.copy(
                name = remoteStore.name,
                address = remoteStore.address,
                city = remoteStore.city,
                phone = remoteStore.phone,
                district = remoteStore.district,
                category = remoteStore.category,
                imageUrl = remoteStore.imageUrl,
            )
            storeRepository.update(store = updatedStore)

            val storeDetail: StoreDetail = remoteStore.toStoreDetail(storeId = store.id)
            storeDetailRepository.update(storeDetail = storeDetail)

            val menuList: List<Menu> = remoteStore.toStoreMenuList(storeId = store.id)
            menuRepository.deleteByStoreId(storeId = store.id)
            menuRepository.createAll(menuList = menuList)
            SyncResult.UPDATE_SUCCESS
        }.onFailure { error ->
            log.error("매장 업데이트 실패 - 이름: ${remoteStore.name}, 주소: ${remoteStore.address}, 원인: ${error.message}")
        }.getOrElse {
            SyncResult.UPDATE_FAILED
        }
    }

    /**
     * 공식 사이트와 API 데이터를 병합합니다.
     *
     * 기본적으로 공식 사이트에서 제공하는 [officialList]를 기준으로 병합하며,
     * API에서 제공하는 [cloudList]의 더 자세한 데이터(메뉴, 지역 정보)로 보완합니다.
     *
     * @param officialList 공식 사이트에서 가져온 매장 목록 (기준 데이터)
     * @param cloudList API에서 가져온 매장 목록 (보완 데이터)
     * @return 병합된 매장 목록
     */
    private fun combineStore(officialList: List<RemoteStore>, cloudList: List<RemoteStore>): List<RemoteStore> {
        val officialMap: Map<String, RemoteStore> = officialList.associateBy { store: RemoteStore -> store.key }
        val cloudMap: Map<String, RemoteStore> = cloudList.associateBy { store: RemoteStore -> store.key }

        return officialMap.mapValues { (key: String, originalStore: RemoteStore) ->
            cloudMap[key]?.let { cloudStore: RemoteStore ->
                originalStore.copy(
                    city = cloudStore.city,
                    district = cloudStore.district,
                    menus = cloudStore.menus.takeIf { it.isNotEmpty() } ?: originalStore.menus
                )
            } ?: originalStore
        }.values.toList()
    }

    companion object {
        private const val DEFAULT_BUFFER = 10
    }
}
