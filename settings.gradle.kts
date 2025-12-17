rootProject.name = "ONGI_Ktor"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://packages.confluent.io/maven/")
    }
}
include("domain")
include("router")
include("schedular")
include("service")
include("data:local")
include("data:remote")
include("model")