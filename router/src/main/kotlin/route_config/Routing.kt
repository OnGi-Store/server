package route_config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import route.*

fun Application.configureRouting() {
    if (isDevMode()) {
        install(plugin = CORS) {
            anyHost()
            allowHeader(header = HttpHeaders.ContentType)
        }
    }

    routing {
        healthCheckRoute()
        syncTimeRoute()
        bannerRoute()
        userRoute()
        storeRoute()
        if (isDevMode()) apiRoute()
    }
}

private fun Route.apiRoute() {
    val path = "swagger"
    val file = "openapi/documentation.yaml"
    swaggerUI(path = path, swaggerFile = file)
}

private fun Application.isDevMode(): Boolean {
    return developmentMode
}
