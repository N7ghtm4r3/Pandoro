
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("maven-publish")
    id("com.android.library") version "8.2.2"
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "2.0.20"
}

group = "com.tecknobit.pandoro"
version = "1.0.5"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            this@jvm.compilerOptions {
                jvmTarget.set(JvmTarget.JVM_18)
            }
        }
    }
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }
    // TODO: PLANNED TO BE IMPLEMENTED IN THE NEXT VERSION
    /*listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Pandoro-Core"
            isStatic = true
        }
    }*/
    // TODO: PLANNED TO BE IMPLEMENTED IN THE NEXT VERSION
    /*@OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
        browser {
            webpackTask {
                dependencies {
                }
            }
        }
    }*/

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation("io.github.n7ghtm4r3:equinox-core:1.0.5")
                implementation("io.github.n7ghtm4r3:equinox-backend:1.0.5")
            }
        }

    }

    jvmToolchain(18)
}

android {
    namespace = "com.tecknobit.pandorocore"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.tecknobit.pandorocore"
                artifactId = "pandorocore"
                version = "1.0.5"
                from(components["kotlin"])
            }
        }
    }
}