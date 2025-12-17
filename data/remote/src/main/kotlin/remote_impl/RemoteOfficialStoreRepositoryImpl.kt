package remote_impl

import Juso
import RemoteStore
import remote_data.store.excel.OfficialStoreDTO
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import remote_mapper.StoreMapper.toRemoteStoreFromOfficial
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import repository.remote.RemoteOfficialStoreRepository
import repository.remote.RemoteRoadAddressRepository
import remote_util.extract.ExcelStoreExtractor

internal class RemoteOfficialStoreRepositoryImpl(
    private val officialHttpClient: HttpClient,
    private val excelStoreExtractor: ExcelStoreExtractor = ExcelStoreExtractor(),
    private val remoteRoadAddressRepository: RemoteRoadAddressRepository,
) : RemoteOfficialStoreRepository {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override suspend fun getStoreListFromOfficial(): List<RemoteStore> = runCatching {
        log.info("📥 행정안전부에서 착한가게 엑셀 다운로드 시작...")

        // 1. 엑셀 다운로드
        val response: HttpResponse = officialHttpClient.post { }
        val data: ByteArray = response.readRawBytes()
        log.info("✅ 엑셀 다운로드 성공 (파일 크기: {} bytes)", data.size)

        // 2. 엑셀 파싱 및 배치 처리
        val stores = excelStoreExtractor.parseExcel(excelData = data)
        log.info("📊 총 ${stores.size}개 매장 처리 시작 (배치 크기: $BATCH_SIZE)")

        coroutineScope {
            stores
                .chunked(size = BATCH_SIZE)
                .flatMap { batch: List<OfficialStoreDTO> ->
                    batch.map { store: OfficialStoreDTO ->
                        async { convertAddressToRoadType(store = store) }
                    }.awaitAll()
                }
                .toRemoteStoreFromOfficial()
        }
    }.getOrElse { e: Throwable ->
        log.error("❌ 엑셀 다운로드 실패: {}", e.message)
        emptyList()
    }

    private suspend fun convertAddressToRoadType(store: OfficialStoreDTO): OfficialStoreDTO {
        val juso: Juso? = remoteRoadAddressRepository.getRoadAddress(rawAddress = store.address).firstOrNull()
        if (juso == null) return store

        return store.copy(address = juso.roadAddress)
    }

    companion object {
        private const val BATCH_SIZE = 1000
    }
}
