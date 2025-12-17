package local_config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.di.*
import local_table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

private const val DRIVER = "org.mariadb.jdbc.Driver"
private const val USER = "DB_USERNAME"
private const val PASSWORD = "DB_PASSWORD"
private const val PORT = "DB_PORT"

fun Application.configureDatabases() {
    val config: ApplicationConfig = environment.config
    val port: String? = System.getenv(PORT)
    val user: String = config.getString(configPath = "database.username", envKey = USER)
    val pwd: String = config.getString(configPath = "database.password", envKey = PASSWORD)
    val url: String = port?.let { "jdbc:mariadb://db:$port/ongi" } ?: config.getProperty("database.url")

    val databaseConfig = HikariConfig().apply {
        jdbcUrl = url
        driverClassName = DRIVER
        username = user
        password = pwd
        maximumPoolSize = 16
        minimumIdle = 4
        connectionTimeout = 30000
        initializationFailTimeout = -1
        validate()
    }
    val database = Database.connect(datasource = HikariDataSource(databaseConfig))

    transaction(db = database) {
        val tables: Array<Table> = arrayOf(
            BannerTable,
            FavoriteTable,
            MenuTable,
            StoreDetailTable,
            StoreTable,
            SyncTimeTable,
            UserTable,
        )

        SchemaUtils.create(tables = tables)
    }

    dependencies {
        provide<Database> { database }
    }
}

private fun ApplicationConfig.getProperty(configPath: String): String {
    return propertyOrNull(configPath)?.getString()
        ?: throw IllegalStateException("설정 값 '$configPath'가 존재하지 않거나 설정되어 있지 않습니다.")
}

private fun ApplicationConfig.getString(configPath: String, envKey: String): String {
    // 1순위: 환경 변수(서버)가 있으면 사용
    // 2순위: application.yaml 값(로컬)을 사용
    return System.getenv(envKey) ?: propertyOrNull(configPath)?.getString()
    ?: throw IllegalStateException("환경 변수 '$envKey'가 설정되어 있지 않습니다.")
}