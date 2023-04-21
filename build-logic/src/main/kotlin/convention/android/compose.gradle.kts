package convention.android

import com.android.build.gradle.BaseExtension

@Suppress("UnstableApiUsage")
configure<BaseExtension> {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
}