package remote_di

import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*
import remote_data.store.api.GoodPriceApiProperties
import remote_impl.*
import remote_util.network.EnvironmentConfig
import remote_util.network.RemoteHttpClientConfig
import repository.remote.*

private const val OFFICIAL_CLIENT = "officialClient"
private const val CLOUD_CLIENT = "cloudClient"
private const val GEOCODER_CLIENT = "geocoderClient"
private const val ROAD_ADDRESS_CLIENT = "roadAddressClient"

fun Application.configureRemoteRepository() {
    val properties: GoodPriceApiProperties = EnvironmentConfig(config = environment.config).loadApiProperties()
    configureHttpClient(properties = properties)
    configureRepository(properties = properties)
}

private fun Application.configureHttpClient(properties: GoodPriceApiProperties) {
    dependencies {
        provide<HttpClient>(name = OFFICIAL_CLIENT) {
            RemoteHttpClientConfig.createOfficialClient(official = properties.official)
        }

        provide<HttpClient>(name = CLOUD_CLIENT) {
            RemoteHttpClientConfig.createCloudClient(cloud = properties.cloud)
        }

        provide<HttpClient>(name = GEOCODER_CLIENT) {
            RemoteHttpClientConfig.createGeocoderClient(geocoder = properties.geocoder)
        }

        provide<HttpClient>(name = ROAD_ADDRESS_CLIENT) {
            RemoteHttpClientConfig.createRoadAddressClient(juso = properties.juso)
        }
    }
}

private fun Application.configureRepository(properties: GoodPriceApiProperties) {
    dependencies {
        provide<RemoteRoadAddressRepository> {
            val httpClient: HttpClient = resolve(key = ROAD_ADDRESS_CLIENT)
            RemoteRoadAddressRepositoryImpl(juso = properties.juso, roadAddressClient = httpClient)
        }

        provide<RemoteStoreBannerRepository> {
            RemoteStoreBannerRepositoryImpl(official = properties.official)
        }

        provide<RemoteOfficialStoreRepository> {
            val httpClient: HttpClient = resolve(key = OFFICIAL_CLIENT)
            val remoteRoadAddressRepository: RemoteRoadAddressRepository = resolve()
            RemoteOfficialStoreRepositoryImpl(
                officialHttpClient = httpClient,
                remoteRoadAddressRepository = remoteRoadAddressRepository
            )
        }

        provide<RemoteAPIStoreRepository> {
            val httpClient: HttpClient = resolve(key = CLOUD_CLIENT)
            val remoteRoadAddressRepository: RemoteRoadAddressRepository = resolve()
            RemoteAPIStoreRepositoryImpl(
                cloud = properties.cloud,
                cloudHttpClient = httpClient,
                remoteRoadAddressRepository = remoteRoadAddressRepository
            )
        }

        provide<RemoteGeocoderRepository> {
            val httpClient: HttpClient = resolve(key = GEOCODER_CLIENT)
            RemoteGeocoderRepositoryImpl(
                geocoder = properties.geocoder,
                geocoderClient = httpClient
            )
        }
    }
}
