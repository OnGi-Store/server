package service.impl

import Favorite
import Store
import User
import repository.local.FavoriteRepository
import repository.local.StoreRepository
import repository.local.UserRepository
import service.FavoriteService
import service.util.TransactionUtil.suspendedTransaction
import java.util.*

internal class FavoriteServiceImpl(
    private val favoriteRepository: FavoriteRepository,
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository,
) : FavoriteService {

    override suspend fun getFavorite(userId: UUID, storeId: UUID): Boolean {
        val favorite: Favorite? = favoriteRepository.findByUserIdAndStoreId(userId = userId, storeId = storeId)
        return favorite != null
    }

    override suspend fun toggleFavorite(userId: UUID, storeId: UUID): Boolean = suspendedTransaction {
        val favorite: Favorite? = favoriteRepository.findByUserIdAndStoreId(userId = userId, storeId = storeId)

        if (favorite == null) likeStore(userId = userId, storeId = storeId)
        else unlikeStore(favorite = favorite)
    }

    private suspend fun likeStore(userId: UUID, storeId: UUID): Boolean {
        val user: User = userRepository.findById(id = userId) ?: makeError(message = NO_USER)
        val store: Store = storeRepository.findById(id = storeId) ?: makeError(message = NO_STORE)
        favoriteRepository.create(user = user, store = store)
        storeRepository.incrementFavoriteCount(id = store.id)
        return true
    }

    private suspend fun unlikeStore(favorite: Favorite): Boolean {
        favoriteRepository.delete(favorite = favorite)
        storeRepository.decrementFavoriteCount(id = favorite.store.id)
        return false
    }

    private fun makeError(message: String): Nothing = throw IllegalStateException(message)


    companion object {
        private const val NO_USER = "사용자를 찾을 수 없습니다."
        private const val NO_STORE = "가게를 찾을 수 없습니다."
    }
}
