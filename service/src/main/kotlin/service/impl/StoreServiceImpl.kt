package service.impl

import Menu
import Store
import StoreDetail
import StorePage
import query.StoreQueryCategory
import query.StoreQueryDistance
import query.StoreQuerySortType
import repository.local.MenuRepository
import repository.local.StoreDetailRepository
import repository.local.StoreRepository
import service.StoreService
import java.util.*

internal class StoreServiceImpl(
    private val storeRepository: StoreRepository,
    private val storeDetailRepository: StoreDetailRepository,
    private val menuRepository: MenuRepository,
) : StoreService {

    override suspend fun getStores(
        userId: UUID,
        latitude: Double,
        longitude: Double,
        page: Int,
        size: Int,
        sortType: StoreQuerySortType,
        category: StoreQueryCategory?,
        distance: StoreQueryDistance?,
        keyword: String?,
        onlyFavorites: Boolean,
    ): StorePage = storeRepository.findStores(
        userId = userId,
        latitude = latitude,
        longitude = longitude,
        sortType = sortType,
        category = category?.columnName,
        distanceRange = distance?.maxDistance,
        keyword = keyword,
        onlyFavorites = onlyFavorites,
        page = page,
        size = size,
    )

    override suspend fun getStoreById(
        storeId: UUID,
        latitude: Double,
        longitude: Double
    ): Store = storeRepository.findStoreByIdWithDistance(
        id = storeId,
        latitude = latitude,
        longitude = longitude
    ) ?: throw RuntimeException(NO_STORE)

    override suspend fun getStoreDetail(storeId: UUID): StoreDetail =
        storeDetailRepository.findByStoreId(storeId = storeId) ?: throw RuntimeException(NO_STORE)

    override suspend fun getStoreMenus(storeId: UUID): List<Menu> = menuRepository.findByStoreId(storeId = storeId)

    override suspend fun contStores(): Long = storeRepository.count()

    companion object {
        private const val NO_STORE = "가게를 찾을 수 없습니다."
    }
}
