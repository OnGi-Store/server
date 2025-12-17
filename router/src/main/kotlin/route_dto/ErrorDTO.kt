package route_dto

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
@OptIn(ExperimentalTime::class)
internal data class ErrorDTO(
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val timestamp: Instant = Clock.System.now()
)
