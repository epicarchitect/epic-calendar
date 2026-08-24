plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.compose)
}

android {
    namespace = "epicarchitect.calendar.compose.sample"
    compileSdk = Constants.androidCompileSdk

    defaultConfig {
        applicationId = "epicarchitect.calendar.compose.sample"
        minSdk = Constants.androidMinSdk
        targetSdk = Constants.androidCompileSdk
        versionCode = 1
        versionName = "1.0"
    }

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
}

dependencies {
    implementation(projects.sample.shared)
    implementation(libs.android.activityCompose)
}
