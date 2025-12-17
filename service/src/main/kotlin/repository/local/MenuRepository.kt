package repository.local

import Menu
import java.util.*

interface MenuRepository {
    suspend fun findByStoreId(storeId: UUID): List<Menu>
    suspend fun deleteByStoreId(storeId: UUID)
    suspend fun createAll(menuList: List<Menu>): List<Menu>
}
