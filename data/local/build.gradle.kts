plugins {
    alias(libs.plugins.kotlin.jvm)
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
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.mariadb.java.client)
    implementation(libs.kotlinx.datetime)
    implementation(libs.hikari.cp)
    testImplementation(libs.kotlin.test.junit)
}

tasks.test {
    useJUnitPlatform()
}