package local_di

import io.ktor.server.application.*
import io.ktor.server.plugins.di.*
import local_repository.impl.BannerRepositoryImpl
import local_repository.impl.FavoriteRepositoryImpl
import local_repository.impl.MenuRepositoryImpl
import local_repository.impl.StoreDetailRepositoryImpl
import local_repository.impl.StoreRepositoryImpl
import local_repository.impl.SyncTimeRepositoryImpl
import local_repository.impl.UserRepositoryImpl
import repository.local.*

fun Application.configureLocalRepository() {
    dependencies {
        provide<BannerRepository> { BannerRepositoryImpl() }
        provide<SyncTimeRepository> { SyncTimeRepositoryImpl() }
        provide<UserRepository> { UserRepositoryImpl() }
        provide<FavoriteRepository> { FavoriteRepositoryImpl() }
        provide<StoreRepository> { StoreRepositoryImpl() }
        provide<StoreDetailRepository> { StoreDetailRepositoryImpl() }
        provide<MenuRepository> { MenuRepositoryImpl() }
    }
}
