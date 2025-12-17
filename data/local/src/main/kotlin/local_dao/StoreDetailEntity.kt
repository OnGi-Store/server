package local_dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import local_table.StoreDetailTable
import java.util.*

internal class StoreDetailEntity(id: EntityID<UUID>) : Entity<UUID>(id = id) {
    companion object : EntityClass<UUID, StoreDetailEntity>(StoreDetailTable)

    var store: StoreEntity by StoreEntity.Companion referencedOn StoreDetailTable.id
    var hasParking: Boolean by StoreDetailTable.hasParking
    var hasTakeout: Boolean by StoreDetailTable.hasTakeout
    var hasDelivery: Boolean by StoreDetailTable.hasDelivery
    var hasReservation: Boolean by StoreDetailTable.hasReservation
    var hasDividedRestroom: Boolean by StoreDetailTable.hasDividedRestroom
    var allowsPets: Boolean by StoreDetailTable.allowsPets
    var hasWifi: Boolean by StoreDetailTable.hasWifi
    var hasKidsFacility: Boolean by StoreDetailTable.hasKidsFacility
    var allowsGroup: Boolean by StoreDetailTable.allowsGroup
}
