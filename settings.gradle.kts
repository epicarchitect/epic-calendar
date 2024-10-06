enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "epic-calendar"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(
    ":sample",
    ":epic-calendar-compose",
)
