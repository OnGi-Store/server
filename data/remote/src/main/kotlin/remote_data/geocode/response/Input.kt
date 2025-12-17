package remote_data.geocode.response

import kotlinx.serialization.Serializable

@Serializable
internal data class Input(
    val type: Type,
    val address: String
)

@Serializable
internal enum class Type {
    ROAD,
    PARCEL
}
