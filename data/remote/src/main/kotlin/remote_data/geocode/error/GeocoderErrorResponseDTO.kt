package remote_data.geocode.error

import kotlinx.serialization.Serializable

@Serializable
internal data class GeocoderErrorResponseDTO(
    val response: GeocoderErrorResponse
)

@Serializable
internal data class GeocoderErrorResponse(
    val service: Service,
    val status: Status,
    val error: Error
)
