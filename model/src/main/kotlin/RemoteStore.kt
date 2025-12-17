data class RemoteStore(
    val name: String,
    val address: String,
    val imageUrl: String?,
    val category: String?,
    val phone: String?,
    val city: String,
    val district: String?,
    val menus: Set<RemoteMenu>,

    val hasParking: Boolean = false,
    val hasTakeout: Boolean = false,
    val hasDelivery: Boolean = false,
    val hasReservation: Boolean = false,
    val hasDividedRestroom: Boolean = false,
    val allowsGroup: Boolean = false,
    val hasWifi: Boolean = false,
    val allowsPets: Boolean = false,
    val hasKidsFacility: Boolean = false,
) {
    val key: String
        get() = "${extractName()} :: ${extractAddress()}"

    private fun extractName(): String {
        val cleanName: String = name.split("\\(")[0]
        val upperName: String = cleanName.uppercase()
        return upperName.replace(regex = "\\s+".toRegex(), replacement = "")
    }

    private fun extractAddress() = address.trim()
}
