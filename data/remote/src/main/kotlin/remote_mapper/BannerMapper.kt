package remote_mapper

import Banner
import remote_data.RemoteBannerDTO

internal object BannerMapper {
    fun RemoteBannerDTO.toBanner() = Banner(
        url = url,
        imageUrl = imageUrl,
    )

    fun List<RemoteBannerDTO>.toBanner() = map { it.toBanner() }
}
