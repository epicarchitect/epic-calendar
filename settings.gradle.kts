enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "epic-calendar"

includeBuild("build-logic")

include(
    ":sample:shared",
    ":sample:android-app",
    ":sample:desktop-app",
    ":library:basis",
//    ":library:pager",
//    ":library:date-picker",
//    ":library:ranges",
)
