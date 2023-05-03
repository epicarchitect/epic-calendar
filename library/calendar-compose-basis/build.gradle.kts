plugins {
    id("convention.multiplatform.publish.library")
    id("org.jetbrains.compose")
}

dependencies {
    commonMainApi("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    commonMainApi(compose.foundation)
    commonMainApi(compose.material3)
}