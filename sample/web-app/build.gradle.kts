plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(projects.sample.shared)
                implementation(compose.html.core)
            }
        }
    }
}

compose.experimental {
    web.application {}
}