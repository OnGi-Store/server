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
    implementation(project(":domain"))
    implementation(libs.ktor.server.di)
    implementation(libs.ktor.task.scheduling.core)
    implementation(libs.ktor.task.scheduling.jdbc)
    testImplementation(libs.kotlin.test.junit)
}

tasks.test {
    useJUnitPlatform()
}
