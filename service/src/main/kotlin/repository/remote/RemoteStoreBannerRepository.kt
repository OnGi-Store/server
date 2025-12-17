package repository.remote

import Banner

interface RemoteStoreBannerRepository {
    suspend fun getStoreBannerList(): List<Banner>
}
