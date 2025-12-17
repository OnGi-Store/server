package local_util

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal object DatabaseUtil {
    private const val ID = "id"
    private const val LENGTH = 36

    fun Table.uuidPrimaryKey(
        name: String = ID,
        length: Int = LENGTH
    ): Column<EntityID<UUID>> = varchar(name = name, length = length).transform(
        wrap = (UUID::fromString),
        unwrap = (UUID::toString)
    ).autoGenerate().entityId()

    fun Table.timestamp(name: String): Column<Instant> {
        return registerColumn(name, KotlinTimeInstantColumnType())
    }
}
