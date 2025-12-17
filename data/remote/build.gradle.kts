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
    implementation(project(":service"))
    implementation(libs.ktor.server.di)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.jsoup)
    implementation(libs.poi)
    implementation(libs.poi.ooxml)
    testImplementation(libs.kotlin.test.junit)
}

tasks.test {
    useJUnitPlatform()
}
