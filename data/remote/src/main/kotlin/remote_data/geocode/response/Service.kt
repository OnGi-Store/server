package remote_data.geocode.response

import kotlinx.serialization.Serializable

@Serializable
internal data class Service(
    val name: String,
    val version: Double,
    val operation: String,
    val time: String
)
