import java.util.UUID

data class Favorite(
    val id: UUID,
    val user: User,
    val store: Store
)
