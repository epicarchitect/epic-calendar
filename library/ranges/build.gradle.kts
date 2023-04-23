plugins {
    id("convention.android.publish.library")
    id("convention.android.compose")
}

android {
    namespace = "epicarchitect.calendar.compose.ranges"
}

dependencies {
    api(projects.library.basis)
}