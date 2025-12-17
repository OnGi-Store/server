package remote_util.network

import remote_data.store.api.Cloud
import remote_data.store.api.Geocoder
import remote_data.store.api.Juso
import remote_data.store.api.Official
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlin.time.Duration.Companion.minutes

internal object RemoteHttpClientConfig {

    fun createOfficialClient(official: Official): HttpClient {
        return HttpClient(engineFactory = OkHttp) {
            install(plugin = ContentNegotiation) {
                json()
            }

            defaultRequest {
                url(urlString = official.base + official.path)
            }
        }
    }

    fun createCloudClient(cloud: Cloud): HttpClient {
        return HttpClient(engineFactory = OkHttp) {
            install(plugin = ContentNegotiation) {
                json()
            }

            defaultRequest {
                url(urlString = cloud.base)
            }
        }
    }

    fun createGeocoderClient(geocoder: Geocoder): HttpClient {
        return HttpClient(engineFactory = OkHttp) {
            install(plugin = ContentNegotiation) {
                json()
            }

            defaultRequest {
                url(urlString = geocoder.base)
            }
        }
    }

    fun createRoadAddressClient(juso: Juso): HttpClient {
        return HttpClient(engineFactory = OkHttp) {
            install(plugin = ContentNegotiation) {
                json()
            }

            install(plugin = HttpTimeout) {
                requestTimeoutMillis = 1.minutes.inWholeMilliseconds
                connectTimeoutMillis = 1.minutes.inWholeMilliseconds
                socketTimeoutMillis = 1.minutes.inWholeMilliseconds
            }

            defaultRequest {
                url(urlString = juso.base)
            }
        }
    }
}
