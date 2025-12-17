package local_mapper

import local_dao.SyncTimeEntity
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal object SyncTimeMapper {
    fun SyncTimeEntity.toInstant(): Instant = time
    fun List<SyncTimeEntity>.toInstant(): List<Instant> = map { it.toInstant() }
}
