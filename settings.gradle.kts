enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "epic-calendar"

includeBuild("build-logic")

include(
    ":sample-app",
    ":library:basis",
    ":library:pager",
    ":library:date-picker",
)
