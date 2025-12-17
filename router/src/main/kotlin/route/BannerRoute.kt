package route

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.di.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import route_dto.BannerDTO
import route_mapper.BannerMapper.toBannerDTO
import service.BannerService

/**
 * Banner API base path
 *
 * 배너 관련 API의 기본 경로
 */
private const val PATH = "/api/v1/banners"

/**
 * 배너 관련 모든 라우트를 등록한다.
 */
internal fun Route.bannerRoute() {
    val bannerService: BannerService by application.dependencies

    registerGetBanners(bannerService = bannerService)
}

/**
 * GET /api/v1/banners
 *
 * 서버 URL을 기준으로 배너 목록을 조회한다.
 */
private fun Route.registerGetBanners(bannerService: BannerService) = get(path = PATH) {
    val serverUrl: String = call.getServerUrl()
    val bannerList: List<BannerDTO> = bannerService.getBannerList(serverUrl = serverUrl).toBannerDTO()
    call.respond(status = HttpStatusCode.OK, message = bannerList)
}

private fun RoutingCall.getServerUrl(): String {
    val origin: RequestConnectionPoint = request.origin
    val scheme: String = origin.scheme
    val host: String = origin.serverHost
    val port: Int = origin.serverPort
    val imageUrl: String = when {
        host == "localhost" -> "$scheme://$host:$port"
        port == 80 || port == 443 || port == -1 -> "https://$host"
        else -> "https://$host:$port"
    }

    return imageUrl
}
