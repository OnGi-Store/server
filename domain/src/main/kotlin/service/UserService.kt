package service

import User
import java.util.*

interface UserService {
    suspend fun findOrCreateUserByAddress(address: String): User
    suspend fun deleteUser(userId: UUID): Boolean
}
