package remote_impl

import Banner
import remote_data.RemoteBannerDTO
import remote_data.store.api.Official
import remote_mapper.BannerMapper.toBanner
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import repository.remote.RemoteStoreBannerRepository
import remote_util.extract.BannerExtractor

internal class RemoteStoreBannerRepositoryImpl(
    private val official: Official,
    private val bannerExtractor: BannerExtractor = BannerExtractor(),
) : RemoteStoreBannerRepository {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override suspend fun getStoreBannerList(): List<Banner> {
        log.info("📥 공행정안전부에서 착한가게 배너 정보 다운로드 시작...")
        val baseUrl: String = official.base
        val remoteBannerList: List<RemoteBannerDTO> = bannerExtractor.extractBanners(url = baseUrl)
        return remoteBannerList.toBanner()
    }
}
