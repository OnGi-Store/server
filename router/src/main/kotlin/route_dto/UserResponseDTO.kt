package route_dto

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
internal data class UserResponseDTO(
    val id: Uuid,
    val address: String
)
