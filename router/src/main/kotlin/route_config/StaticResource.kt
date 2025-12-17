package route_config

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

private const val REMOTE_IMAGE_PATH = "/images"
private const val LOCAL_IMAGE_PATH = "static/images"

fun Application.configureStaticResources() {
    routing {
        staticResources(remotePath = REMOTE_IMAGE_PATH, basePackage = LOCAL_IMAGE_PATH)
    }
}
