package remote_data.geocode.response

import kotlinx.serialization.Serializable

@Serializable
internal data class GeocoderResponseDTO(
    val response: GeocoderResponse
)

@Serializable
internal data class GeocoderResponse(
    val service: Service,
    val status: Status,
    val input: Input,
    val refined: Refined,
    val result: Result
)
