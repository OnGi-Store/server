package route_dto

import kotlinx.serialization.Serializable

@Serializable
internal data class StorePageDTO(
    val stores: List<StoreDTO>,
    val hasNext: Boolean,
    val hasPrev: Boolean
)
