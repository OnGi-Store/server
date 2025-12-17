package remote_util.network

import remote_data.store.api.*
import io.ktor.server.config.*

private const val GOOD_PRICE_API_BASE_URL = "GOOD_PRICE_API_BASE_URL"
private const val GOOD_PRICE_API_PATH = "GOOD_PRICE_API_PATH"

private const val GOOD_CLOUD_API_BASE_URL = "GOOD_CLOUD_API_BASE_URL"
private const val GOOD_CLOUD_API_VERSION = "GOOD_CLOUD_API_VERSION"
private const val GOOD_CLOUD_API_KEY = "GOOD_CLOUD_API_KEY"

private const val GEOCODER_API_URL = "GEOCODER_API_URL"
private const val GEOCODER_API_KEY = "GEOCODER_API_KEY"

private const val JUSO_API_BASE_URL = "JUSO_API_BASE_URL"
private const val JUSO_API_KEY = "JUSO_API_KEY"

internal class EnvironmentConfig(private val config: ApplicationConfig) {

    private fun getEnv(configPath: String, envKey: String): String {
        // 서버 환경변수가 있으면 그걸 쓰고, 없으면 yaml 파일 값을 씁니다.
        return System.getenv(envKey) ?: config.tryGetString(configPath)
        ?: throw IllegalStateException("환경 변수 '$envKey'가 설정되어 있지 않습니다.")
    }

    fun loadApiProperties() = GoodPriceApiProperties(
        official = Official(
            base = getEnv(envKey = GOOD_PRICE_API_BASE_URL, configPath = "goodprice.api.official.base"),
            path = getEnv(envKey = GOOD_PRICE_API_PATH, configPath = "goodprice.api.official.path"),
        ),
        cloud = Cloud(
            base = getEnv(envKey = GOOD_CLOUD_API_BASE_URL, configPath = "goodprice.api.cloud.base"),
            version = getEnv(envKey = GOOD_CLOUD_API_VERSION, configPath = "goodprice.api.cloud.version"),
            key = getEnv(envKey = GOOD_CLOUD_API_KEY, configPath = "goodprice.api.cloud.key"),
        ),
        geocoder = Geocoder(
            base = getEnv(envKey = GEOCODER_API_URL, configPath = "goodprice.api.geocoder.base"),
            key = getEnv(envKey = GEOCODER_API_KEY, configPath = "goodprice.api.geocoder.key"),
        ),
        juso = Juso(
            base = getEnv(envKey = JUSO_API_BASE_URL, configPath = "goodprice.api.juso.base"),
            key = getEnv(envKey = JUSO_API_KEY, configPath = "goodprice.api.juso.key"),
        )
    )
}
