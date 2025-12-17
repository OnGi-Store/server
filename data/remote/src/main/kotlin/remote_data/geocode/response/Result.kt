package remote_data.geocode.response

import kotlinx.serialization.Serializable

@Serializable
internal data class Result(
    val crs: String,
    val point: RemotePoint
)

@Serializable
internal data class RemotePoint(
    val x: Double,
    val y: Double
)
