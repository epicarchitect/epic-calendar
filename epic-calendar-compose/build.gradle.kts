import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrains.multiplatform)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.compose)
    id("maven-publish")
    id("signing")
}

val libraryVersion = "1.1.0"

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    jvmToolchain(Constants.jvmToolchainVersion)
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    android {
        namespace = "epicarchitect.calendar.compose"
        compileSdk = Constants.androidCompileSdk
        minSdk = Constants.androidMinSdk
    }
    iosArm64()
    iosSimulatorArm64()

    js {
        browser()
        binaries.executable()
    }

    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.jetbrains.datetime)
            api(libs.jetbrains.composeMaterial3)
            api(libs.jetbrains.composeUi)
            api(libs.jetbrains.composeFoundation)
        }
    }
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    group = "build"
    description = "Assembles an empty javadoc jar for Maven publication."
    archiveClassifier.set("javadoc")
}

publishing {
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
            credentials {
                username = findProperty("sonatype.username") as String?
                password = findProperty("sonatype.password") as String?
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications.withType<MavenPublication>().configureEach {
        artifact(javadocJar)
        groupId = "io.github.epicarchitect"
        version = libraryVersion

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
    useGpgCmd()
    sign(publishing.publications)
}

tasks.withType<AbstractPublishToMaven> {
    dependsOn(tasks.withType<Sign>())
}
