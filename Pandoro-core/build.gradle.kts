plugins {
    id("java")
    id("maven-publish")
    kotlin("jvm") version "1.9.20-RC2"
}

group = "com.tecknobit"
version = "1.0.4"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.clojars.org")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.3")
    implementation("org.springframework:spring-web:6.1.5")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.4")
    implementation("com.github.N7ghtm4r3:APIManager:2.2.1")
    implementation("org.json:json:20231013")
    implementation("commons-validator:commons-validator:1.7")
    implementation("com.github.N7ghtm4r3:Equinox:1.0.1")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.tecknobit.pandorocore"
                artifactId = "Pandoro-core"
                version = "1.0.4"
                from(components["java"])
            }
        }
    }
}

configurations.all {
    exclude("commons-logging", "commons-logging")
}

kotlin {
    jvmToolchain(18)
}