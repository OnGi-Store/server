package service.di

import io.ktor.server.application.*
import io.ktor.server.plugins.di.*
import repository.local.*
import repository.remote.RemoteAPIStoreRepository
import repository.remote.RemoteGeocoderRepository
import repository.remote.RemoteOfficialStoreRepository
import repository.remote.RemoteStoreBannerRepository
import service.*
import service.impl.*

fun Application.configureService() {
    val syncTimeRepository: SyncTimeRepository by dependencies
    val bannerRepository: BannerRepository by dependencies
    val userRepository: UserRepository by dependencies
    val favoriteRepository: FavoriteRepository by dependencies
    val storeRepository: StoreRepository by dependencies
    val storeDetailRepository: StoreDetailRepository by dependencies
    val menuRepository: MenuRepository by dependencies

    val remoteStoreBannerRepository: RemoteStoreBannerRepository by dependencies
    val remoteOfficialStoreRepository: RemoteOfficialStoreRepository by dependencies
    val remoteAPIStoreRepository: RemoteAPIStoreRepository by dependencies
    val remoteGeocoderRepository: RemoteGeocoderRepository by dependencies

    dependencies {
        provide<SyncTimeService> {
            SyncTimeServiceImpl(syncTimeRepository = syncTimeRepository)
        }

        provide<BannerService> {
            BannerServiceImpl(bannerRepository = bannerRepository)
        }

        provide<UserService> {
            UserServiceImpl(
                userRepository = userRepository,
                favoriteRepository = favoriteRepository,
                storeRepository = storeRepository,
            )
        }

        provide<FavoriteService> {
            FavoriteServiceImpl(
                userRepository = userRepository,
                favoriteRepository = favoriteRepository,
                storeRepository = storeRepository,
            )
        }

        provide<StoreService> {
            StoreServiceImpl(
                storeRepository = storeRepository,
                storeDetailRepository = storeDetailRepository,
                menuRepository = menuRepository
            )
        }

        provide<SyncStoreService> {
            SyncStoreServiceImpl(
                bannerRepository = bannerRepository,
                storeRepository = storeRepository,
                storeDetailRepository = storeDetailRepository,
                menuRepository = menuRepository,
                remoteStoreBannerRepository = remoteStoreBannerRepository,
                remoteOfficialStoreRepository = remoteOfficialStoreRepository,
                remoteAPIStoreRepository = remoteAPIStoreRepository,
                remoteGeocoderRepository = remoteGeocoderRepository
            )
        }
    }
}
