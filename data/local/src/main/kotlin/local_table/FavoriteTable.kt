package local_table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import local_util.DatabaseUtil.uuidPrimaryKey
import java.util.*

private const val FAVORITE_TABLE = "favorite"
private const val STORE_ID = "store_id"
private const val USER_ID = "user_id"
private const val UNIQUE_NAME = "unique_user_store"

internal object FavoriteTable : IdTable<UUID>(name = FAVORITE_TABLE) {
    override val id: Column<EntityID<UUID>> = uuidPrimaryKey()

    val storeId: Column<EntityID<UUID>> = reference(
        name = STORE_ID,
        foreign = StoreTable,
        onDelete = ReferenceOption.CASCADE
    )

    val userId: Column<EntityID<UUID>> = reference(
        name = USER_ID,
        foreign = UserTable,
        onDelete = ReferenceOption.CASCADE
    )

    init {
        uniqueIndex(customIndexName = UNIQUE_NAME, userId, storeId)
    }
}
