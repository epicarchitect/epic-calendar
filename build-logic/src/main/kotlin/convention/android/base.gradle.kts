package convention.android

import com.android.build.gradle.BaseExtension
import convention.Constants
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

configure<BaseExtension> {
    namespace = "${Constants.ANDROID_BASE_NAMESPACE}.${name.filter { it != '-' }}"
    compileSdkVersion(Constants.ANDROID_TARGET_SDK)

    defaultConfig {
        minSdk = Constants.ANDROID_MIN_SDK
        targetSdk = Constants.ANDROID_TARGET_SDK
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(Constants.JVM_TARGET)
        targetCompatibility = JavaVersion.toVersion(Constants.JVM_TARGET)
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Constants.JVM_TARGET.toString()
    }
}