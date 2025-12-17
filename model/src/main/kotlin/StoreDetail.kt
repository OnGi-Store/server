import java.util.*

data class StoreDetail(
    val storeId: UUID,
    val hasParking: Boolean,
    val hasTakeout: Boolean,
    val hasDelivery: Boolean,
    val hasReservation: Boolean,
    val hasDividedRestroom: Boolean,
    val allowsPets: Boolean,
    val hasWifi: Boolean,
    val hasKidsFacility: Boolean,
    val allowsGroup: Boolean
)
