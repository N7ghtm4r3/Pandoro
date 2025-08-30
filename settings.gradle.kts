rootProject.name = "Pandoro"

pluginManagement {
    plugins {
        kotlin("jvm") version "2.2.10"
        kotlin("multiplatform") version "2.2.10"
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include("core")
include("backend")
