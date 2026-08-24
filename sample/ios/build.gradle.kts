plugins {
    alias(libs.plugins.jetbrains.multiplatform)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "KMPLib"
            isStatic = true
        }
    }

    sourceSets {
        iosMain.dependencies {
            implementation(projects.sample.shared)
            implementation(libs.jetbrains.composeUi)
        }
    }
}
