package route_dto

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
internal data class StoreDTO(
    val id: Uuid,
    val name: String,
    val address: String,
    val favoriteCount: Int,
    val latitude: Double,
    val longitude: Double,
    val category: String?,
    val phone: String?,
    val city: String?,
    val district: String?,
    val imageUrl: String?,
    val distance: Double
)
