plugins {
    id("convention.multiplatform.publish.library")
    id("org.jetbrains.compose")
}

dependencies {
    commonMainApi(projects.library.calendarComposePager)
    commonMainApi(projects.library.calendarComposeRanges)
}