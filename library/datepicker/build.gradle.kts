plugins {
    id("convention.multiplatform.publish.library")
    id("org.jetbrains.compose")
}

dependencies {
    commonMainApi(projects.library.pager)
    commonMainApi(projects.library.ranges)
}