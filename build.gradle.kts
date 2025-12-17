plugins {
    application
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.aloe_droid"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(project(":schedular"))
    implementation(project(":router"))

    implementation(project(":domain"))
    implementation(project(":service"))

    implementation(project(":data:local"))
    implementation(project(":data:remote"))

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
