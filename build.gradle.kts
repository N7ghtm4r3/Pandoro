plugins {
    id("java")
    id("maven-publish")
    id("org.springframework.boot") version "3.1.5"
    kotlin("jvm") version "1.9.20-RC2"
}

apply(plugin = "io.spring.dependency-management")

group = "com.tecknobit"
version = "0.0.0.5"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    implementation("com.github.N7ghtm4r3:APIManager:2.1.7")
    implementation("org.json:json:20230227")
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.4")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")
    implementation("commons-validator:commons-validator:1.7")
}

tasks {

    val sourcesJar by creating(Jar::class) {
        dependsOn(JavaPlugin.CLASSES_TASK_NAME)
        from(java.sourceSets["main"].allSource)
    }

    val javadocJar by creating(Jar::class) {
        dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
        from(java.docsDir)
    }

    artifacts {
        add("archives", sourcesJar)
        add("archives", javadocJar)
    }

}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("Maven") {
                groupId = "com.tecknobit.pandoro"
                artifactId = "Pandoro"
                version = "0.0.0.5"
                from(components["java"])
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(19)
}