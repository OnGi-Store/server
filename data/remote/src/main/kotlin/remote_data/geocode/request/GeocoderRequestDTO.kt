package remote_data.geocode.request

import kotlinx.serialization.Serializable
import kotlin.reflect.full.memberProperties

/**
 * 참고할 요청
 * <a href="https://www.vworld.kr/dev/v4dv_geocoderguide2_s001.do"> DTO 정보 </a>
 */
@Serializable
data class GeocoderRequestDTO(
    val key: String,
    val address: String,
    val service: String = "address",
    val version: String = "2.0",
    val request: String = "GetCoord",
    val format: String = "json",
    val errorFormat: String = "json",
    val type: String = "ROAD",
    val refine: Boolean = true,
    val simple: Boolean = false,
    val crs: String = "EPSG:4326",
) {
    /**
     * DTO 필드들을 Ktor/HTTP 클라이언트에서 사용하기 쉬운 파라미터 맵으로 변환합니다.
     * 리플렉션을 사용하지만, Java Reflection보다 안전한 Kotlin Reflection을 사용합니다.
     */
    fun toParamMap(): Map<String, String> = this::class.memberProperties.associate { prop ->
        val key: String = prop.name
        val value: String = prop.getter.call(this).toString()
        key to value
    }
}
