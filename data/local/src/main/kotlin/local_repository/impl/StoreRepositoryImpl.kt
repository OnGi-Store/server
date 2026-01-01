package local_repository.impl

import Store
import StorePage
import StoreWithDistance
import local_dao.StoreEntity
import local_mapper.StoreMapper.toStore
import local_mapper.StoreMapper.toStoreWithDistance
import local_repository.util.DoubleExpression
import local_repository.util.RepositoryUtil.calculateDistance
import local_repository.util.RepositoryUtil.dbQuery
import local_repository.util.SortUtil.toSortPair
import local_table.FavoriteTable
import local_table.StoreTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateStatement
import query.StoreQuerySortType
import repository.local.StoreRepository
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class StoreRepositoryImpl : StoreRepository {

    override suspend fun create(store: Store): Store = dbQuery {
        StoreEntity.new {
            applyStore(store = store)
        }.toStore()
    }

    override suspend fun update(store: Store): Store = dbQuery {
        val entity = StoreEntity.find { StoreTable.id eq store.id }
            .singleOrNull()
            ?: throw NoSuchElementException("ID ${store.id}를 가진 Store를 찾을 수 없습니다. 업데이트할 수 없습니다.")

        entity
            .applyStore(store = store)
            .toStore()
    }

    override suspend fun count(): Long = dbQuery {
        StoreEntity.count()
    }

    override suspend fun incrementFavoriteCount(id: UUID) {
        StoreTable.update(where = { StoreTable.id eq id }) { updateStatement: UpdateStatement ->
            with(receiver = SqlExpressionBuilder) {
                updateStatement.update(column = favoriteCount, value = favoriteCount + 1)
            }
        }
    }

    override suspend fun decrementFavoriteCount(id: UUID) {
        StoreTable.update(where = { StoreTable.id eq id }) { updateStatement: UpdateStatement ->
            with(receiver = SqlExpressionBuilder) {
                updateStatement.update(column = favoriteCount, value = favoriteCount - 1)
            }
        }
    }

    override suspend fun findById(id: UUID): Store? = dbQuery {
        StoreEntity
            .findById(id = id)
            ?.toStore()
    }

    override suspend fun findByNameAndAddress(
        name: String,
        address: String
    ): Store? = dbQuery {
        StoreEntity
            .find { (StoreTable.name eq name) and (StoreTable.address eq address) }
            .firstOrNull()
            ?.toStore()
    }

    override suspend fun findStoreByIdWithDistance(
        id: UUID,
        latitude: Double,
        longitude: Double
    ): StoreWithDistance? = dbQuery {
        StoreEntity.findById(id = id)?.let { entity: StoreEntity ->
            val distance: Double = calculateDistance(
                lat1 = latitude,
                lon1 = longitude,
                lat2 = entity.latitude,
                lon2 = entity.longitude
            )

            entity.toStoreWithDistance(distance = distance)
        }
    }

    override suspend fun findStores(
        userId: UUID,
        latitude: Double,
        longitude: Double,
        sortType: StoreQuerySortType,
        category: String?,
        distanceRange: Double?,
        keyword: String?,
        onlyFavorites: Boolean,
        page: Int,
        size: Int
    ): StorePage = dbQuery {
        val offset: Long = (page - 1) * size.toLong()
        val limit: Int = size + 1

        // 거리 계산
        val distanceSQL =
            "(6371 * acos(cos(radians($latitude)) * cos(radians(${StoreTable.latitude.name})) * " +
                    "cos(radians(${StoreTable.longitude.name}) - radians($longitude)) + " +
                    "sin(radians($latitude)) * sin(radians(${StoreTable.latitude.name}))))"

        val distanceExpr = DoubleExpression(expr = distanceSQL).alias(alias = "distance")

        // join
        val baseTable: ColumnSet = if (onlyFavorites) {
            StoreTable.join(
                otherTable = FavoriteTable,
                joinType = JoinType.LEFT,
                additionalConstraint = {
                    (StoreTable.id eq FavoriteTable.storeId) and (FavoriteTable.userId eq userId)
                }
            )
        } else {
            StoreTable
        }

        val columns: List<Expression<*>> = listOf(
            StoreTable.id,
            StoreTable.name,
            StoreTable.address,
            StoreTable.favoriteCount,
            StoreTable.latitude,
            StoreTable.longitude,
            StoreTable.category,
            StoreTable.phone,
            StoreTable.city,
            StoreTable.district,
            StoreTable.imageUrl,
            distanceExpr
        )

        val query: Query = baseTable.select(columns = columns).apply {
            if (!category.isNullOrBlank()) andWhere { StoreTable.category eq category }
            if (!keyword.isNullOrBlank()) andWhere { StoreTable.name like "%$keyword%" }
            if (onlyFavorites) andWhere { FavoriteTable.userId eq userId }
            if (distanceRange != null) andWhere { DoubleExpression(expr = distanceSQL) lessEq distanceRange }

            // sort & paging
            orderBy(sortType.toSortPair(distanceExpr = distanceExpr))
            limit(count = limit)
            offset(start = offset)
        }

        // 결과 매핑
        val rows: List<StoreWithDistance> = query.map { row: ResultRow ->
            val store = Store(
                id = row[StoreTable.id].value,
                name = row[StoreTable.name],
                address = row[StoreTable.address],
                favoriteCount = row[StoreTable.favoriteCount],
                latitude = row[StoreTable.latitude],
                longitude = row[StoreTable.longitude],
                category = row[StoreTable.category],
                phone = row[StoreTable.phone],
                city = row[StoreTable.city],
                district = row[StoreTable.district],
                imageUrl = row[StoreTable.imageUrl],
            )
            val distance: Double = row[distanceExpr]
            StoreWithDistance(store = store, distance = distance)
        }

        val hasNext: Boolean = rows.size > size
        val stores: List<StoreWithDistance> = if (hasNext) rows.dropLast(n = 1) else rows
        val hasPrev: Boolean = page > 1

        StorePage(
            stores = stores,
            hasNext = hasNext,
            hasPrev = hasPrev
        )
    }

    private fun StoreEntity.applyStore(store: Store): StoreEntity {
        name = store.name
        address = store.address
        favoriteCount = store.favoriteCount
        latitude = store.latitude
        longitude = store.longitude
        category = store.category
        phone = store.phone
        city = store.city
        district = store.district
        imageUrl = store.imageUrl
        updatedAt = Clock.System.now()
        return this
    }
}
