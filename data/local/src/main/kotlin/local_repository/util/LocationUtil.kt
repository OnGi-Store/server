package local_repository.util

import ch.hsr.geohash.GeoHash

/**
 * 위치 기반 검색을 위한 Geohash 인코딩 유틸리티입니다.
 */
internal object LocationUtil {

    // 저장 시 사용하는 기본 정밀도 (약 150m x 150m 오차 범위)
    private const val STORAGE_PRECISION = 7

    /**
     * 중심 좌표와 8방향 인접 셀의 Geohash를 반환합니다.
     *
     * @param distanceKm 검색 반경 (단위: km)
     * @param latitude 중심 위도
     * @param longitude 중심 경도
     * @return 중심 셀과 8개 인접 셀의 Geohash 문자열 리스트 (총 9개)
     */
    fun encodeWithNeighbors(distanceKm: Double, latitude: Double, longitude: Double): List<String> {
        val precision: Int = getPrecisionFromDistance(distanceKm = distanceKm)
        val centerGeohash: GeoHash = GeoHash.withCharacterPrecision(latitude, longitude, precision)

        return buildList {
            add(element = centerGeohash.toBase32())
            centerGeohash.adjacent.forEach { adjacentGeohash: GeoHash ->
                add(element = adjacentGeohash.toBase32())
            }
        }
    }

    /**
     * 저장용 Geohash 생성 (최대 정밀도)
     */
    fun encodeForStorage(latitude: Double, longitude: Double): String {
        return GeoHash.geoHashStringWithCharacterPrecision(latitude, longitude, STORAGE_PRECISION)
    }

    /**
     * 거리(km)를 기반으로 Geohash 정밀도(문자 길이)를 결정합니다.
     * Geohash 셀의 크기는 위도에 따라 다르지만, 일반적으로 다음 기준을 따릅니다.
     */
    private fun getPrecisionFromDistance(distanceKm: Double): Int = when {
        distanceKm <= 0.005 -> 8    // ~19m
        distanceKm <= 0.15 -> 7     // ~152m
        distanceKm <= 1.2 -> 6      // ~1.2km
        distanceKm <= 4.9 -> 5      // ~4.9km
        distanceKm <= 39.7 -> 4     // ~39.7km
        distanceKm <= 156.0 -> 3    // ~156km
        else -> 2                   // ~1250km
    }
}
