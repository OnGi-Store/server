package local_repository.util

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SortOrder
import query.StoreQuerySortType
import local_table.StoreTable

internal object SortUtil {
    fun StoreQuerySortType.toSortPair(distanceExpr: Expression<Double>): Pair<Expression<*>, SortOrder> =
        when (this) {
            StoreQuerySortType.NAME -> StoreTable.name to SortOrder.ASC
            StoreQuerySortType.FAVORITE -> StoreTable.favoriteCount to SortOrder.DESC
            StoreQuerySortType.DISTANCE -> distanceExpr to SortOrder.ASC
        }
}
