package repository.local

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface SyncTimeRepository {
    suspend fun create(time: Instant): Instant
    suspend fun getAll(): List<Instant>
    suspend fun deleteAll(): Int
}
