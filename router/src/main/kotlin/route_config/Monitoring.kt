package route_config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(plugin = CallLogging) {
        level = Level.INFO
        filter { call: ApplicationCall ->
            val status: HttpStatusCode? = call.response.status()
            status != null
            status == null || status != HttpStatusCode.NotFound
        }
    }
}
