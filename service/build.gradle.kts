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
    implementation(libs.exposed.core)
    testImplementation(libs.kotlin.test.junit)
}

tasks.test {
    useJUnitPlatform()
}
