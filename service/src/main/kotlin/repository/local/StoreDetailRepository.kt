package repository.local

import StoreDetail
import java.util.*

interface StoreDetailRepository {
    suspend fun findByStoreId(storeId: UUID): StoreDetail?
    suspend fun create(storeDetail: StoreDetail): StoreDetail
    suspend fun update(storeDetail: StoreDetail): StoreDetail
}
