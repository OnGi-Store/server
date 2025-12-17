package route_dto

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
internal data class MenuDTO(
    val id: Uuid,
    val storeId: Uuid,
    val name: String,
    val price: String,
)
