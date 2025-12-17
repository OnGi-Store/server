package remote_impl

import Juso
import RemoteStore
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import remote_data.store.api.Cloud
import remote_data.store.api.CloudPageDTO
import remote_data.store.api.CloudStoreDTO
import remote_mapper.StoreMapper.toRemoteStoreFromCloud
import remote_util.network.NetworkUtil.retryWithDelay
import repository.remote.RemoteAPIStoreRepository
import repository.remote.RemoteRoadAddressRepository

internal class RemoteAPIStoreRepositoryImpl(
    private val cloud: Cloud,
    private val cloudHttpClient: HttpClient,
    private val remoteRoadAddressRepository: RemoteRoadAddressRepository,
) : RemoteAPIStoreRepository {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override suspend fun getStoreListFromAPI(): List<RemoteStore> = runCatching {
        log.info("📥 공공데이터 API에서 착한가게 데이터 다운로드 시작...")

        // 1. 전체 데이터 확인
        val totalCount: Int = fetchTotalCount()
        // 2. 데이터 추출
        val storeList: List<CloudStoreDTO> = fetchData(totalCount = totalCount)

        log.info("✅ 공공데이터 API 수집 성공 (가게 : {} 개)", storeList.size)
        storeList.toRemoteStoreFromCloud()
    }.getOrElse { e: Throwable ->
        log.error("❌ 공공데이터 API 수집 실패: {}", e.message)
        emptyList()
    }

    private suspend fun fetchTotalCount(): Int = retryWithDelay {
        cloudHttpClient.get {
            url {
                path(cloud.version)
                parameters.append("page", "1")
                parameters.append("perPage", "1")
                parameters.append("returnType", "JSON")
                parameters.append("serviceKey", cloud.key)
            }
        }.body<CloudPageDTO>().totalCount
    }

    private suspend fun fetchData(totalCount: Int): List<CloudStoreDTO> = retryWithDelay {
        val response: CloudPageDTO = cloudHttpClient.get {
            url {
                path(cloud.version)
                parameters.append("page", "1")
                parameters.append("perPage", totalCount.toString())
                parameters.append("returnType", "JSON")
                parameters.append("serviceKey", cloud.key)
            }
        }.body()

        log.info("📦 공공데이터 API 응답 수신 완료 — 총 ${response.data.size}건 확인됨 (배치 크기: $BATCH_SIZE)")

        coroutineScope {
            response.data
                .chunked(size = BATCH_SIZE)
                .flatMap { batch: List<CloudStoreDTO> ->
                    batch.map { store: CloudStoreDTO ->
                        async { convertAddressToRoadType(store) }
                    }.awaitAll()
                }
        }
    }

    private suspend fun convertAddressToRoadType(store: CloudStoreDTO): CloudStoreDTO {
        if (store.address == null) return store
        val juso: Juso? = remoteRoadAddressRepository.getRoadAddress(rawAddress = store.address).firstOrNull()
        if (juso == null) return store

        val districtValue: String = when {
            juso.sigunguName.isNotBlank() -> juso.sigunguName
            juso.eubMyeonDongName.isNotBlank() -> juso.eubMyeonDongName
            else -> juso.roadAddress
        }

        return store.copy(
            address = juso.roadAddress,
            city = juso.sigunguName.ifEmpty { store.city },
            district = districtValue
        )
    }

    companion object {
        private const val BATCH_SIZE = 1000
    }
}
