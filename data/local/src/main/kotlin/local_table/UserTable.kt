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

private const val USER_TABLE = "user"
private const val CREATED_AT = "createdAt"
private const val ADDRESS = "address"

@OptIn(ExperimentalTime::class)
internal object UserTable : IdTable<UUID>(name = USER_TABLE) {
    override val id: Column<EntityID<UUID>> = uuidPrimaryKey()
    val createdAt: Column<Instant> = timestamp(name = CREATED_AT).clientDefault { Clock.System.now() }
    val address: Column<String> = varchar(name = ADDRESS, length = 255)
}
