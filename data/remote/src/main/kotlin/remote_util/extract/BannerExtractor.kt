package remote_util.extract

import remote_data.RemoteBannerDTO
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.slf4j.LoggerFactory

internal class BannerExtractor {
    private val log = LoggerFactory.getLogger(javaClass)

    fun extractBanners(url: String): List<RemoteBannerDTO> = runCatching {
        val banners: MutableSet<RemoteBannerDTO> = mutableSetOf()
        val doc: Document = Jsoup.connect(url).get()
        val items: Elements = doc.select(".nmain_banner .owl-carousel .item")

        items.forEach { item ->
            val anchor: Element? = item.selectFirst("a")
            val image: Element? = item.selectFirst("img")

            if (anchor == null || image == null) {
                log.warn("⚠️ 배너 정보가 누락되었습니다.")
                return@forEach
            }

            val anchorUrl: String = anchor.absUrl("href")
            val imageUrl: String = image.absUrl("src")

            banners += RemoteBannerDTO(
                url = anchorUrl,
                imageUrl = imageUrl
            )
        }

        banners.toList()
    }.getOrElse { e: Throwable ->
        log.error("❌ HTML 요청 실패: ${e.message}", e)
        emptyList()
    }
}
