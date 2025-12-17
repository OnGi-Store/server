package local_dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import local_table.BannerTable
import java.util.*

internal class BannerEntity(id: EntityID<UUID>) : UUIDEntity(id = id) {
    companion object : UUIDEntityClass<BannerEntity>(table = BannerTable)

    var url: String by BannerTable.url
    var imageUrl: String by BannerTable.imageUrl
}
