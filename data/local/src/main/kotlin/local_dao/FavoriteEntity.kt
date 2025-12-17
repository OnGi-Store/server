package local_dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import local_table.FavoriteTable
import java.util.*

internal class FavoriteEntity(id: EntityID<UUID>) : UUIDEntity(id = id) {
    companion object : UUIDEntityClass<FavoriteEntity>(table = FavoriteTable)

    var store by StoreEntity.Companion referencedOn FavoriteTable.storeId
    var user by UserEntity.Companion referencedOn FavoriteTable.userId
}
