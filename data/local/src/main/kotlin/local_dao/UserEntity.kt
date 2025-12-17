package local_dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import local_table.UserTable
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class UserEntity(id: EntityID<UUID>) : UUIDEntity(id = id) {
    companion object : UUIDEntityClass<UserEntity>(table = UserTable)

    var createdAt: Instant by UserTable.createdAt
    var address: String by UserTable.address
}
