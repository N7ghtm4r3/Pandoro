plugins {
    id("java")
    kotlin("jvm")
    id("org.springframework.boot") version "3.2.3"
}

apply(plugin = "io.spring.dependency-management")

group = "com.tecknobit.pandoro"
version = "1.0.5"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.clojars.org")
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.3")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.3")
    implementation("org.springframework.boot:spring-boot-maven-plugin:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.3")
    implementation("com.github.N7ghtm4r3:APIManager:2.2.4")
    implementation("io.github.n7ghtm4r3:equinox-backend:1.0.5")
    implementation("io.github.n7ghtm4r3:equinox-core:1.0.5")
    implementation("org.json:json:20240303")
    implementation(project(":core"))
}

kotlin {
    jvmToolchain(18)
}

java {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
}