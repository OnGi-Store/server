package local_repository.impl

import Favorite
import Store
import User
import local_dao.FavoriteEntity
import local_mapper.FavoriteMapper.toFavorite
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.InsertStatement
import repository.local.FavoriteRepository
import local_repository.util.RepositoryUtil.dbQuery
import local_table.FavoriteTable
import java.util.*

internal class FavoriteRepositoryImpl : FavoriteRepository {
    override suspend fun findByUserId(userId: UUID): List<Favorite> = dbQuery {
        FavoriteEntity
            .find { (FavoriteTable.userId eq userId) }
            .toList()
            .toFavorite()
    }

    override suspend fun findByUserIdAndStoreId(
        userId: UUID,
        storeId: UUID
    ): Favorite? = dbQuery {
        FavoriteEntity
            .find { (FavoriteTable.userId eq userId) and (FavoriteTable.storeId eq storeId) }
            .singleOrNull()
            ?.toFavorite()
    }

    override suspend fun create(user: User, store: Store): Favorite = dbQuery {
        val result: InsertStatement<Number> = FavoriteTable.insert { insertStatement: InsertStatement<Number> ->
            insertStatement[userId] = user.id
            insertStatement[storeId] = store.id
        }
        val id: UUID = result[FavoriteTable.id].value
        Favorite(id = id, user = user, store = store)
    }

    override suspend fun delete(favorite: Favorite) = dbQuery {
        FavoriteTable.deleteWhere {
            FavoriteTable.id eq favorite.id
        }
    }
}
