package remote_data.geocode.response

import kotlinx.serialization.Serializable

@Serializable
internal enum class Status {
    OK,
    NOT_FOUND,
    ERROR,
}
