package route

import io.ktor.http.*
import io.ktor.server.plugins.di.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import service.SyncTimeService
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Time synchronization API base path
 *
 * 서버와의 시간 동기화를 위한 API 경로
 */
private const val PATH = "/api/v1/time"

/**
 * 시간 동기화 관련 모든 라우트를 등록한다.
 */
@OptIn(ExperimentalTime::class)
internal fun Route.syncTimeRoute() {
    val syncTimeService: SyncTimeService by application.dependencies
    registerGetSyncTime(syncTimeService = syncTimeService)
}

/**
 * GET /api/v1/time
 *
 * 서버 기준 현재 동기화 시간을 반환한다.
 */
@OptIn(ExperimentalTime::class)
private fun Route.registerGetSyncTime(syncTimeService: SyncTimeService) = get(path = PATH) {
    val time: Instant = syncTimeService.getSyncTime()
    call.respond(status = HttpStatusCode.OK, message = time)
}
