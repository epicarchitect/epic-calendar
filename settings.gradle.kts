enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "epic-calendar"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

includeBuild("build-logic")

include(
    ":sample:shared",
    ":sample:android-app",
    ":sample:desktop-app",
    ":sample:web-app",
    ":library:calendar-compose-basis",
    ":library:calendar-compose-pager",
    ":library:calendar-compose-datepicker",
    ":library:calendar-compose-ranges",
)
