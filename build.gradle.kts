buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath(":build-logic")
    }
}


allprojects {
    repositories {
        google()
        mavenCentral()
    }
}