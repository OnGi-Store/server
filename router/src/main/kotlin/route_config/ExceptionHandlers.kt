package route_config

import route_dto.ErrorDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Application.configureExceptionHandlers() {
    install(plugin = StatusPages) {
        exception<BadRequestException> { call: ApplicationCall, ex: Throwable ->
            val badRequest: HttpStatusCode = HttpStatusCode.BadRequest
            call.respond(
                status = badRequest,
                message = ErrorDTO(
                    status = badRequest.value,
                    error = badRequest.description,
                    message = ex.message ?: badRequest.description,
                    path = call.request.path()
                )
            )
        }

        exception<Throwable> { call: ApplicationCall, ex: Throwable ->
            val internalError: HttpStatusCode = HttpStatusCode.InternalServerError
            call.respond(
                status = internalError,
                message = ErrorDTO(
                    status = internalError.value,
                    error = internalError.description,
                    message = ex.message ?: internalError.description,
                    path = call.request.path()
                )
            )
        }
    }
}
