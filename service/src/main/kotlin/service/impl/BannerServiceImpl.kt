package service.impl

import Banner
import repository.local.BannerRepository
import service.BannerService

internal class BannerServiceImpl(private val bannerRepository: BannerRepository) : BannerService {

    override suspend fun getBannerList(serverUrl: String): List<Banner> {
        val bannerList: List<Banner> = bannerRepository.findAll()
        return listOf(makeDefaultBanner(serverUrl = serverUrl)) + bannerList
    }

    private fun makeDefaultBanner(serverUrl: String): Banner {
        val baseImageUrl = "$serverUrl/images/default-banner.png"
        val baseUrl = "https://goodprice.go.kr/"
        return Banner(url = baseUrl, imageUrl = baseImageUrl)
    }
}
