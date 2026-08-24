import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrains.multiplatform)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.compose)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    jvmToolchain(Constants.jvmToolchainVersion)
    android {
        namespace = "epicarchitect.sample.shared"
        compileSdk = Constants.androidCompileSdk
        minSdk = Constants.androidMinSdk
    }
    jvm()

    js {
        browser()
    }

    wasmJs {
        browser()
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.jetbrains.composeMaterial3)
            implementation(projects.epicCalendarCompose)
        }
    }
}
