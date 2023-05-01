//plugins {
//    id("convention.android.publish.library")
//    id("convention.android.compose")
//}
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("convention.android.base")
    id("org.jetbrains.compose")
}

kotlin {
    android()
    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js {
        browser()
        binaries.executable()
    }
}

android {
    namespace = "epicarchitect.calendar.compose.pager"
}

dependencies {
    commonMainApi(projects.library.basis)
}