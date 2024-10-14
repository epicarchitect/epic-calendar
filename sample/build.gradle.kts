import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.multiplatform)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    androidTarget()
    jvm()

    js {
        browser()
        binaries.executable()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "KMPLib"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.android.activityCompose)
        }

        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(compose.ui)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        commonMain.dependencies {
            implementation(compose.material3)
            implementation(project(":epic-calendar-compose"))
        }
    }
}

android {
    namespace = "epicarchitect.sample.shared"
    compileSdk = 34

    defaultConfig {
        applicationId = "epicarchitect.calendar.compose.sample"
        minSdk = 26
        targetSdk = 34
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "android-proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

compose {
    desktop {
        application {
            mainClass = "epicarchitect.sample.MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "SampleApp"
                packageVersion = "1.0.0"
            }
        }
    }
}