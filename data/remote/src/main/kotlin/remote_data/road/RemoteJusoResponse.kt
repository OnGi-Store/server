package remote_data.road

import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteJusoResponse(
    val results: RemoteJusoResult
)
