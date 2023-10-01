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
        getByName("jsMain") {
            dependencies {
                implementation(projects.sample.shared)
                implementation(compose.html.core)
                implementation(compose.ui)
            }
        }
    }
}

compose.experimental {
    web.application {}
}