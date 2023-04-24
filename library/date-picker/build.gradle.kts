plugins {
    id("convention.android.publish.library")
    id("convention.android.compose")
}

android {
    namespace = "epicarchitect.calendar.compose.datePicker"
}

dependencies {
    api(projects.library.pager)
    api(projects.library.ranges)
}