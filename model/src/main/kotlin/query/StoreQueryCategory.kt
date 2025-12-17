package query

enum class StoreQueryCategory(val columnName: String) {
    KOREAN_FOOD(columnName = "한식"),
    WESTERN_FOOD(columnName = "양식"),
    JAPANESE_FOOD(columnName = "일식"),
    CHINESE_FOOD(columnName = "중식"),
    BAKERY(columnName = "베이커리"),
    RESTAURANT(columnName = "기타요식업"),
    BATH_HOUSE(columnName = "목욕업"),
    LAUNDRY(columnName = "세탁업"),
    HOTEL(columnName = "숙박업"),
    HAIR_SALON(columnName = "미용업"),
    ETC(columnName = "기타비요식업");
}
