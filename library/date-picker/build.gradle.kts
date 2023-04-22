plugins {
    id("convention.android.publish.library")
    id("convention.android.compose")
}

android {
    namespace = "epicarchitect.calendar.compose.datepicker"
}

dependencies {
    api(projects.library.pager)
}