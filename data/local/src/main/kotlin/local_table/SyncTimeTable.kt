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

private const val SYNC_TIME_TABLE = "sync_time"
private const val TIME = "time"

@OptIn(ExperimentalTime::class)
internal object SyncTimeTable : IdTable<UUID>(SYNC_TIME_TABLE) {
    override val id: Column<EntityID<UUID>> = uuidPrimaryKey()
    override val primaryKey = PrimaryKey(columns = arrayOf(id))
    val time: Column<Instant> = timestamp(TIME).clientDefault { Clock.System.now() }
}
