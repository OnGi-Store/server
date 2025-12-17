package route_mapper

import User
import route_dto.UserResponseDTO
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

@OptIn(ExperimentalUuidApi::class)
internal object UserMapper {
    fun User.toUserResponseDTO() = UserResponseDTO(
        id = id.toKotlinUuid(),
        address = address
    )
}
