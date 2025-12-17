package query

/**
 * 거리 제한은 km(킬로미터) 단위
 */
enum class StoreQueryDistance(val maxDistance: Double) {
    M_5(maxDistance = 0.5),
    K_1(maxDistance = 1.0),
    K_3(maxDistance = 3.0),
    K_5(maxDistance = 5.0),
    K_10(maxDistance = 10.0),
    K_15(maxDistance = 15.0),
    K_20(maxDistance = 20.0),
    K_30(maxDistance = 30.0),
    K_40(maxDistance = 40.0),
    K_50(maxDistance = 50.0),
    K_60(maxDistance = 60.0);
}
