package query

enum class StoreQuerySortType(val columnName: String) {
    NAME(columnName = "name"),
    FAVORITE(columnName = "favoriteCount"),
    DISTANCE(columnName = "distance");
}
