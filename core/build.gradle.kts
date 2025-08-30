@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("maven-publish")
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "2.0.20"
    alias(libs.plugins.androidLibrary)
}

group = "com.tecknobit.pandoro"
version = "1.2.0"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            this@jvm.compilerOptions {
                jvmTarget.set(JvmTarget.JVM_18)
            }
        }
    }
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "pandorocore"
            isStatic = true
        }
    }

    wasmJs {
        binaries.executable()
        browser {
            webpackTask {
                dependencies {
                }
            }
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(libs.equinox.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }

    }

    jvmToolchain(18)
}

android {
    namespace = "com.tecknobit.pandorocore"
    compileSdk = 36
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
                version = "1.2.0"
                from(components["kotlin"])
            }
        }
    }
}