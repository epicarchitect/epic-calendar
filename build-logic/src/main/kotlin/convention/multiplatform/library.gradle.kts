package convention.multiplatform

import convention.Constants

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("convention.android.base")
}

kotlin {
    androidTarget()
    jvm("desktop")
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        create("iosMain") {
            dependsOn(getByName("commonMain"))
            getByName("iosX64Main").dependsOn(this)
            getByName("iosArm64Main").dependsOn(this)
            getByName("iosSimulatorArm64Main").dependsOn(this)
        }
    }

    jvmToolchain(Constants.JVM_TARGET)
}