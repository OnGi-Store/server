package route_dto

import kotlinx.serialization.Serializable

@Serializable
internal data class BannerDTO(
    val url: String,
    val imageUrl: String,
)
