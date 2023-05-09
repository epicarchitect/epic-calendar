plugins {
    id("convention.multiplatform.publish.library")
    id("org.jetbrains.compose")
}

dependencies {
    commonMainApi(libs.kotlin.datetime)
    commonMainApi(compose.foundation)
    commonMainApi(compose.material3)
}