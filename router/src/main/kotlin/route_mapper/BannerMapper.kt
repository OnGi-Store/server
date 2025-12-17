package route_mapper

import Banner
import route_dto.BannerDTO

internal object BannerMapper {
    fun Banner.toBannerDTO() = BannerDTO(
        url = url,
        imageUrl = imageUrl,
    )

    fun List<Banner>.toBannerDTO(): List<BannerDTO> = map { it.toBannerDTO() }
}
