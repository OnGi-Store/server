package remote_data.road

import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteJusoResult(
    val common: RemoteCommonResponse,
    val juso: List<RemoteJuso>?
)
