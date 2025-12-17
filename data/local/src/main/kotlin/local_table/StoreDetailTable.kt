package local_table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

private const val STORE_DETAIL_TABLE = "store_detail"
private const val STORE_ID = "store_id"
private const val HAS_PARKING = "hasParking"
private const val HAS_TAKEOUT = "hasTakeout"
private const val HAS_DELIVERY = "hasDelivery"
private const val HAS_RESERVATION = "hasReservation"
private const val HAS_DIVIDED_RESTROOM = "hasDividedRestroom"
private const val ALLOWS_PETS = "allowsPets"
private const val HAS_WIFI = "hasWifi"
private const val HAS_KIDS_FACILITY = "hasKidsFacility"
private const val ALLOWS_GROUP = "allowsGroup"

internal object StoreDetailTable : IdTable<UUID>(name = STORE_DETAIL_TABLE) {
    override val id: Column<EntityID<UUID>> = reference(name = STORE_ID, foreign = StoreTable)
    val hasParking: Column<Boolean> = bool(name = HAS_PARKING).default(defaultValue = false)
    val hasTakeout: Column<Boolean> = bool(name = HAS_TAKEOUT).default(defaultValue = false)
    val hasDelivery: Column<Boolean> = bool(name = HAS_DELIVERY).default(defaultValue = false)
    val hasReservation: Column<Boolean> = bool(name = HAS_RESERVATION).default(defaultValue = false)
    val hasDividedRestroom: Column<Boolean> = bool(name = HAS_DIVIDED_RESTROOM).default(defaultValue = false)
    val allowsPets: Column<Boolean> = bool(name = ALLOWS_PETS).default(defaultValue = false)
    val hasWifi: Column<Boolean> = bool(name = HAS_WIFI).default(defaultValue = false)
    val hasKidsFacility: Column<Boolean> = bool(name = HAS_KIDS_FACILITY).default(defaultValue = false)
    val allowsGroup: Column<Boolean> = bool(name = ALLOWS_GROUP).default(defaultValue = false)
}
