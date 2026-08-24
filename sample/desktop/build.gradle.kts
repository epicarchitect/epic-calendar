import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.jetbrains.multiplatform)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    jvmToolchain(Constants.jvmToolchainVersion)
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(projects.sample.shared)
            implementation(compose.desktop.currentOs)
        }
    }
}

compose {
    desktop {
        application {
            mainClass = "epicarchitect.sample.desktop.MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "SampleApp"
                packageVersion = "1.0.0"
            }
        }
    }
}
