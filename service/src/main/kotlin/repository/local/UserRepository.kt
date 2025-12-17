package repository.local

import User
import java.util.*

interface UserRepository {
    suspend fun findByAddress(address: String): User?
    suspend fun findById(id: UUID): User?
    suspend fun create(address: String): User
    suspend fun deleteById(id: UUID)
}
