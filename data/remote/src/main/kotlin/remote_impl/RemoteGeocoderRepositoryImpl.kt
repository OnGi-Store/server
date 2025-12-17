package remote_impl

import Point
import remote_data.geocode.error.GeocoderErrorResponseDTO
import remote_data.geocode.request.GeocoderRequestDTO
import remote_data.geocode.response.GeocoderResponseDTO
import remote_data.geocode.response.Status
import remote_data.store.api.Geocoder
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import remote_mapper.StoreMapper.toPoint
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import repository.remote.RemoteGeocoderRepository

internal class RemoteGeocoderRepositoryImpl(
    private val geocoder: Geocoder,
    private val geocoderClient: HttpClient
) : RemoteGeocoderRepository {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override suspend fun getStoreLocation(address: String): Point = runCatching {
        val requestDTO = GeocoderRequestDTO(key = geocoder.key, address = address)
        val httpResponse: HttpResponse = geocoderClient.get {
            url {
                requestDTO.toParamMap().forEach { (key: String, value: String) ->
                    parameters.append(name = key, value = value)
                }
            }
        }
        val responseText: String = httpResponse.bodyAsText()
        val jsonElement: JsonElement = Json.parseToJsonElement(string = responseText)
        val status = jsonElement.jsonObject["response"]
            ?.jsonObject?.get("status")
            ?.jsonPrimitive?.content
            ?.let { Status.valueOf(value = it) }
            ?: throw IllegalStateException("status를 찾을 수 없습니다")

        if (status == Status.ERROR) {
            val errorResponse = Json.decodeFromString<GeocoderErrorResponseDTO>(string = responseText)
            throw RuntimeException(errorResponse.response.error.toString())
        }else if(status == Status.NOT_FOUND) {
            throw RuntimeException("주소 검색 실패")
        }

        val json = Json { ignoreUnknownKeys = true }
        json.decodeFromString<GeocoderResponseDTO>(responseText)
            .response
            .result
            .point
            .toPoint()
    }.getOrElse { e: Throwable ->
        val errorMessage = "주소(\"$address\")에 대한 지오코딩 요청 중 오류가 발생했습니다. (원인: ${e.message})"
        throw RuntimeException(errorMessage)
    }
}
