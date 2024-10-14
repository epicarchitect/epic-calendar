import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.multiplatform)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.compose)
    id("maven-publish")
    id("signing")
}

kotlin {
    androidTarget {
        publishAllLibraryVariants()
        publishLibraryVariantsGroupedByFlavor = true
    }
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.jetbrains.datetime)
            api(compose.material3)
        }
    }
}

android {
    namespace = "epicarchitect.calendar.compose"
    compileSdk = 34
    defaultConfig.minSdk = 26
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

fun extString(name: String) = ext[name]!!.toString()

project.rootProject.file("local.properties").reader().use {
    Properties().apply { load(it) }
}.onEach { (name, value) ->
    ext[name.toString()] = value
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = extString("ossrhUsername")
                password = extString("ossrhPassword")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications.withType<MavenPublication> {
        artifact(javadocJar)
        groupId = "io.github.epicarchitect"
        version = "1.0.6"

        pom {
            name.set("Epic Calendar")
            description.set("Compose Multiplatform library for displaying epic calendars")
            url.set("https://github.com/epicarchitect/epic-calendar")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("epicarchitect")
                    name.set("Alexander Kolmachikhin")
                    email.set("alexanderkolmachikhin@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/epicarchitect/epic-calendar")
            }
        }
    }
}

signing {
    sign(publishing.publications)
}

tasks.withType<AbstractPublishToMaven> {
    dependsOn(tasks.withType<Sign>())
}