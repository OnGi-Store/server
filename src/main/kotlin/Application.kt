import io.ktor.server.application.*
import io.ktor.server.netty.*
import local_config.configureDatabases
import local_di.configureLocalRepository
import remote_di.configureRemoteRepository
import route_config.*
import scheduler.configureTaskScheduling
import service.di.configureService

fun main(args: Array<String>) {
    EngineMain.main(args = args)
}

fun Application.module() {
    // 1. repository config
    configureDatabases()
    configureLocalRepository()
    configureRemoteRepository()

    // 2. service config
    configureService()

    // 3. route config
    configureJsonHandlers()
    configureExceptionHandlers()
    configureStaticResources()
    configureRouting()
    configureMonitoring()

    // 4. schedular config
    configureTaskScheduling()
}
