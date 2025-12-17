package remote_data.store.excel

internal data class OfficialStoreDTO(
    val name: String,
    val address: String,
    val category: String? = null,
    val phone: String? = null,
    val menu: String? = null,
    val menuPrice: String? = null,
    val hasParking: Boolean,
    val hasTakeout: Boolean,
    val hasDelivery: Boolean,
    val hasReservation: Boolean,
    val hasDividedRestroom: Boolean,
    val allowsGroup: Boolean,
    val hasWifi: Boolean,
    val allowsPets: Boolean,
    val hasKidsFacility: Boolean,
    val imageUrl: String? = null
)
