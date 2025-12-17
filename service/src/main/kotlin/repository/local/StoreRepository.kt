package repository.local

import Store
import StorePage
import query.StoreQuerySortType
import java.util.*

interface StoreRepository {
    suspend fun create(store: Store): Store
    suspend fun update(store: Store): Store
    suspend fun count(): Long
    suspend fun incrementFavoriteCount(id: UUID)
    suspend fun decrementFavoriteCount(id: UUID)
    suspend fun findById(id: UUID): Store?
    suspend fun findByNameAndAddress(name: String, address: String): Store?
    suspend fun findStoreByIdWithDistance(id: UUID, latitude: Double, longitude: Double): Store?
    suspend fun findStores(
        userId: UUID,
        latitude: Double,
        longitude: Double,
        sortType: StoreQuerySortType,
        category: String?,
        distanceRange: Double?,
        keyword: String?,
        onlyFavorites: Boolean,
        page: Int,
        size: Int
    ): StorePage
}
