package convention.android

import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

configure<BaseExtension> {
    compileSdkVersion(Versions.TARGET_ANDROID_SDK)

    defaultConfig {
        minSdk = Versions.MIN_ANDROID_SDK
        targetSdk = Versions.TARGET_ANDROID_SDK
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(Versions.JVM_TARGET)
        targetCompatibility = JavaVersion.toVersion(Versions.JVM_TARGET)
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Versions.JVM_TARGET
    }
}