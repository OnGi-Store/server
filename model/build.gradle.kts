plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "com.aloe_droid"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotlin.test.junit)
}

tasks.test {
    useJUnitPlatform()
}