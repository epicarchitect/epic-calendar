plugins {
    id("convention.android.library")
    id("convention.android.compose")
}

android {
    namespace = "epicarchitect.calendar.compose"
}

dependencies {
    api("androidx.compose.material3:material3:1.0.1")
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    api("androidx.compose.foundation:foundation:1.4.1")
}