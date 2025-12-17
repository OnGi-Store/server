package service

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface SyncTimeService {
    suspend fun getSyncTime(): Instant
    suspend fun setSyncTime()
}
