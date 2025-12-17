plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.aloe_droid"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":model"))
    implementation(project(":domain"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.ktor.server.di)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.call.logging)
    testImplementation(libs.kotlin.test.junit)
}

tasks.test {
    useJUnitPlatform()
}