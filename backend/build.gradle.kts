plugins {
    id("java")
    id("maven-publish")
    id("org.springframework.boot") version "3.2.3"
}

apply(plugin = "io.spring.dependency-management")

group = "com.tecknobit"
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
    implementation("io.github.n7ghtm4r3:equinox-backend:1.0.5")
    implementation("io.github.n7ghtm4r3:equinox-core:1.0.5")
}