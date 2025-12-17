package local_repository.impl

import Menu
import local_dao.MenuEntity
import local_dao.StoreEntity
import local_mapper.StoreMapper.toMenu
import repository.local.MenuRepository
import local_repository.util.RepositoryUtil.dbQuery
import local_table.MenuTable
import local_table.StoreTable
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class MenuRepositoryImpl : MenuRepository {

    override suspend fun findByStoreId(storeId: UUID): List<Menu> = dbQuery {
        MenuEntity
            .find { MenuTable.storeId eq storeId }
            .toMenu()
    }

    override suspend fun deleteByStoreId(storeId: UUID) = dbQuery {
        MenuEntity
            .find { MenuTable.storeId eq storeId }
            .forEach { menuEntity: MenuEntity -> menuEntity.delete() }
    }

    override suspend fun createAll(menuList: List<Menu>): List<Menu> = dbQuery {
        if (menuList.isEmpty()) return@dbQuery emptyList()
        val storeId: UUID = menuList.first().storeId
        val storeEntity = StoreEntity.find { StoreTable.id eq storeId }
            .singleOrNull()
            ?: throw NoSuchElementException("ID ${storeId}를 가진 StoreDetail을 찾을 수 없습니다.")

        menuList.map { menu: Menu ->
            MenuEntity.new {
                store = storeEntity
                name = menu.name
                price = menu.price
                updatedAt = Clock.System.now()
            }.toMenu()
        }
    }
}
