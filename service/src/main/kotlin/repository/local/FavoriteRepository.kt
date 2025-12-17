package repository.local

import Favorite
import Store
import User
import java.util.*

interface FavoriteRepository {
    suspend fun findByUserId(userId: UUID): List<Favorite>
    suspend fun findByUserIdAndStoreId(userId: UUID, storeId: UUID): Favorite?
    suspend fun create(user: User, store: Store): Favorite
    suspend fun delete(favorite: Favorite): Int
}
