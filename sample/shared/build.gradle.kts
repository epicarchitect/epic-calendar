plugins {
    id("convention.multiplatform.library")
    kotlin("native.cocoapods")
    id("org.jetbrains.compose")
}

kotlin {
    cocoapods {
        version = "1.0.0"
        summary = "Epic summary"
        homepage = "Epic link"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../../sample/ios-app/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }
}

dependencies {
    commonMainImplementation(projects.library.datepicker)
}