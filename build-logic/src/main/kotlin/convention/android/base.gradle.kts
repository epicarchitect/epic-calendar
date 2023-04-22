package convention.android

import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

configure<BaseExtension> {
    compileSdkVersion(Constants.TARGET_ANDROID_SDK)

    defaultConfig {
        minSdk = Constants.MIN_ANDROID_SDK
        targetSdk = Constants.TARGET_ANDROID_SDK
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(Constants.JVM_TARGET)
        targetCompatibility = JavaVersion.toVersion(Constants.JVM_TARGET)
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Constants.JVM_TARGET
    }
}