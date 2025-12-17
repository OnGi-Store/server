package remote_impl

import Juso
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import remote_data.road.RemoteJusoResponse
import remote_mapper.JusoMapper.toJuso
import remote_util.address.AddressUtil.cleanedAddress
import repository.remote.RemoteRoadAddressRepository
import kotlin.time.Duration.Companion.seconds

internal class RemoteRoadAddressRepositoryImpl(
    private val juso: remote_data.store.api.Juso,
    private val roadAddressClient: HttpClient
) : RemoteRoadAddressRepository {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun getRoadAddress(rawAddress: String): Flow<Juso> = flow {
        val key: String = juso.key
        val address: String = rawAddress.cleanedAddress()
        val response: RemoteJusoResponse = roadAddressClient.get {
            url {
                parameters.append(name = "confmKey", value = key)
                parameters.append(name = "currentPage", value = "1")
                parameters.append(name = "countPerPage", value = "1")
                parameters.append(name = "keyword", value = address)
                parameters.append(name = "resultType", value = "json")
            }
        }.body<RemoteJusoResponse>()
        val juso: Juso = response.results.juso?.firstOrNull()?.toJuso() ?: Juso(
            roadAddress = address,
            sigunguName = "",
            eubMyeonDongName = ""
        )
        emit(value = juso)
    }.retryWhen { cause: Throwable, attempt: Long ->
        if (attempt < 3) {
            val delayTime = (2L.seconds * (attempt.toInt() + 1)).coerceAtMost(maximumValue = 8.seconds)
            delay(duration = delayTime)
            true
        } else {
            false
        }
    }.catch { error: Throwable ->
        log.error("🚨 주소 검색 실패 (무시하고 진행) | address='${rawAddress}'")
    }.flowOn(context = Dispatchers.IO)
}
