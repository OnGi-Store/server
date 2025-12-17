package remote_data.store.api

internal data class GoodPriceApiProperties(
    val official: Official,
    val cloud: Cloud,
    val geocoder: Geocoder,
    val juso: Juso
)

internal data class Official(
    val base: String,
    val path: String
)

internal data class Cloud(
    val base: String,
    val version: String,
    val key: String
)

internal data class Geocoder(
    val base: String,
    val key: String
)

internal data class Juso(
    val base: String,
    val key: String
)
