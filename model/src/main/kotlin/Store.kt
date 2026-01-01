import java.util.*

data class Store(
    val id: UUID,
    val name: String,
    val address: String,
    val favoriteCount: Int,
    val latitude: Double,
    val longitude: Double,
    val category: String?,
    val phone: String?,
    val city: String,
    val district: String?,
    val imageUrl: String?,
)
