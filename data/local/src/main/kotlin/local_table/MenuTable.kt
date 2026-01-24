package local_table

import local_util.DatabaseUtil.timestamp
import local_util.DatabaseUtil.uuidPrimaryKey
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private const val MENU_TABLE = "menu"
private const val STORE_ID = "store_id"
private const val NAME = "name"
private const val PRICE = "price"
private const val UPDATED_AT = "updatedAt"

@OptIn(ExperimentalTime::class)
internal object MenuTable : IdTable<UUID>(name = MENU_TABLE) {
    override val id: Column<EntityID<UUID>> = uuidPrimaryKey()
    override val primaryKey = PrimaryKey(columns = arrayOf(id))
    val storeId: Column<EntityID<UUID>> = reference(name = STORE_ID, foreign = StoreTable)
    val name: Column<String> = varchar(name = NAME, length = 255)
    val price: Column<String> = varchar(name = PRICE, length = 255)
    val updatedAt: Column<Instant> = timestamp(name = UPDATED_AT).clientDefault { Clock.System.now() }
}
