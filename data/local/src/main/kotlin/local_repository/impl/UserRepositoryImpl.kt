package local_repository.impl

import User
import local_dao.UserEntity
import local_mapper.UserMapper.toUser
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import repository.local.UserRepository
import local_repository.util.RepositoryUtil.dbQuery
import local_table.UserTable
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class UserRepositoryImpl : UserRepository {
    override suspend fun findByAddress(address: String): User? = dbQuery {
        UserEntity
            .find { UserTable.address eq address }
            .singleOrNull()
            ?.toUser()
    }

    override suspend fun findById(id: UUID): User? = dbQuery {
        UserEntity.findById(id = id)?.toUser()
    }

    override suspend fun create(address: String): User = dbQuery {
        UserEntity.new {
            this.address = address
            this.createdAt = Clock.System.now()
        }.toUser()
    }

    override suspend fun deleteById(id: UUID) = dbQuery {
        UserTable.deleteWhere { UserTable.id eq id }
        return@dbQuery
    }
}
