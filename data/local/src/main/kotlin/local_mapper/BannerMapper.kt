package local_mapper

import Banner
import local_dao.BannerEntity

internal object BannerMapper {
    fun BannerEntity.toBanner() = Banner(
        url = url,
        imageUrl = imageUrl
    )
}
