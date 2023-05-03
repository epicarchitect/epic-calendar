plugins {
    id("convention.multiplatform.publish.library")
    id("org.jetbrains.compose")
}

dependencies {
    commonMainApi(projects.library.calendarComposeBasis)
}