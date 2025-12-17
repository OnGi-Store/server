package route_config

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureJsonHandlers() {
    install(plugin = ContentNegotiation) {
        json()
    }
}
