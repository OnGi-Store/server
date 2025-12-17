package service

import Banner

interface BannerService {
    suspend fun getBannerList(serverUrl: String): List<Banner>
}
