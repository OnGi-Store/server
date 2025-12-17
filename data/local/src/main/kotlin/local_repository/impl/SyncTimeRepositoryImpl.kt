package local_repository.impl

import local_dao.SyncTimeEntity
import local_mapper.SyncTimeMapper.toInstant
import org.jetbrains.exposed.sql.deleteAll
import repository.local.SyncTimeRepository
import local_repository.util.RepositoryUtil.dbQuery
import local_table.SyncTimeTable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class SyncTimeRepositoryImpl : SyncTimeRepository {
    override suspend fun create(time: Instant): Instant = dbQuery {
        SyncTimeEntity.new {
            this@new.time = time
        }.toInstant()
    }

    override suspend fun getAll(): List<Instant> = dbQuery {
        SyncTimeEntity.all().toList().toInstant()
    }

    override suspend fun deleteAll() = dbQuery {
        SyncTimeTable.deleteAll()
    }
}
