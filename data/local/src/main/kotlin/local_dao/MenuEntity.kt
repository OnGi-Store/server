package local_dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import local_table.MenuTable
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class MenuEntity(id: EntityID<UUID>) : UUIDEntity(id = id) {
    companion object : UUIDEntityClass<MenuEntity>(table = MenuTable)

    var store: StoreEntity by StoreEntity.Companion referencedOn MenuTable.storeId
    var name: String by MenuTable.name
    var price: String by MenuTable.price
    var updatedAt: Instant by MenuTable.updatedAt
}
