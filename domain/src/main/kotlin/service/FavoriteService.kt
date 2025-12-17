package service

import java.util.*

interface FavoriteService {
    suspend fun getFavorite(userId: UUID, storeId: UUID): Boolean
    suspend fun toggleFavorite(userId: UUID, storeId: UUID): Boolean
}
