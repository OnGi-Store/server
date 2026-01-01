package service

import Menu
import StoreDetail
import StorePage
import StoreWithDistance
import query.StoreQueryCategory
import query.StoreQueryDistance
import query.StoreQuerySortType
import java.util.*

interface StoreService {

    suspend fun getStores(
        userId: UUID,
        latitude: Double,
        longitude: Double,
        page: Int,
        size: Int,
        sortType: StoreQuerySortType,
        category: StoreQueryCategory?,
        distance: StoreQueryDistance?,
        keyword: String?,
        onlyFavorites: Boolean,
    ): StorePage

    suspend fun getStoreById(storeId: UUID, latitude: Double, longitude: Double): StoreWithDistance

    suspend fun getStoreDetail(storeId: UUID): StoreDetail

    suspend fun getStoreMenus(storeId: UUID): List<Menu>

    suspend fun contStores(): Long
}
