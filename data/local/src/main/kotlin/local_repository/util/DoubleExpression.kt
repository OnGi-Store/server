package local_repository.util

import org.jetbrains.exposed.sql.DoubleColumnType
import org.jetbrains.exposed.sql.Function
import org.jetbrains.exposed.sql.QueryBuilder

internal class DoubleExpression(val expr: String) : Function<Double>(DoubleColumnType()) {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder.append(value = expr)
    }
}
