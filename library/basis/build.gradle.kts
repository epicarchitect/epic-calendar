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

    sourceSets {
        val commonMain by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    namespace = "epicarchitect.calendar.compose.basis"
}

dependencies {
    commonMainApi("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    commonMainApi(compose.foundation)
    commonMainApi(compose.material3)
}