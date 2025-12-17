package service.impl

import Favorite
import User
import repository.local.FavoriteRepository
import repository.local.StoreRepository
import repository.local.UserRepository
import service.UserService
import service.util.TransactionUtil.suspendedTransaction
import java.util.*

internal class UserServiceImpl(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val storeRepository: StoreRepository
) : UserService {

    override suspend fun findOrCreateUserByAddress(address: String): User = suspendedTransaction {
        val user: User? = userRepository.findByAddress(address)
        user?.let { user } ?: userRepository.create(address = address)
    }

    override suspend fun deleteUser(userId: UUID) = suspendedTransaction {
        runCatching {
            val favoriteList: List<Favorite> = favoriteRepository.findByUserId(userId = userId)
            favoriteList.forEach { favorite: Favorite ->
                storeRepository.decrementFavoriteCount(id = favorite.store.id)
                favoriteRepository.delete(favorite = favorite)
            }
            userRepository.deleteById(id = userId)
        }.fold(
            onSuccess = { true },
            onFailure = { false }
        )
    }
}
