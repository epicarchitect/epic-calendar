enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "epic-calendar"

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
