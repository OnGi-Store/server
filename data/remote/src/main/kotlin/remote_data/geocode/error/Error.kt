package remote_data.geocode.error

import kotlinx.serialization.Serializable

@Serializable
internal data class Error(
    val level: Long,
    val code: String,
    val text: String
)
