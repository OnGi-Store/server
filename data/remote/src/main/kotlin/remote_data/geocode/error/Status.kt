package remote_data.geocode.error

import kotlinx.serialization.Serializable

@Serializable
internal enum class Status {
    OK,
    NOT_FOUND,
    ERROR
}
