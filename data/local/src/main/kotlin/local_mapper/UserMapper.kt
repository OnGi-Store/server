package local_mapper

import User
import local_dao.UserEntity

internal object UserMapper {
    fun UserEntity.toUser() = User(
        id = id.value,
        address = address
    )
}
