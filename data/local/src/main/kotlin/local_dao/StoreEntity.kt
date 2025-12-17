package local_dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import local_table.StoreTable
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class StoreEntity(id: EntityID<UUID>) : UUIDEntity(id = id) {
    companion object : UUIDEntityClass<StoreEntity>(table = StoreTable)

    var name: String by StoreTable.name
    var address: String by StoreTable.address
    var favoriteCount: Int by StoreTable.favoriteCount
    var latitude: Double by StoreTable.latitude
    var longitude: Double by StoreTable.longitude
    var updatedAt: Instant by StoreTable.updatedAt
    var category: String? by StoreTable.category
    var phone: String? by StoreTable.phone
    var city: String by StoreTable.city
    var district: String? by StoreTable.district
    var imageUrl: String? by StoreTable.imageUrl
}
