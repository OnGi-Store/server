package route

import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Health check API path
 *
 * Nginx 헬스체크 용도로 사용되는 엔드포인트
 */
private const val health = "/health"

/**
 * Nginx 헬스체크를 위한 라우트를 등록한다.
 */
internal fun Route.healthCheckRoute() {
    get(path = health) {
        call.respondText(text = "OK")
    }
}
