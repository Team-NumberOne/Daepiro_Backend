plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("org.jlleitschuh.gradle.ktlint-idea") version "10.3.0"
}

// 버전 관리
object VERSIONS {
    const val MYSQL_CONNECTOR = "8.0.26"
    const val AWS_SPRING_CLOUD = "2.4.4"
    const val JAVA = "17"
    const val KOTLIN_LOGGING = "3.0.5"
    const val SWAGGER = "2.2.0"
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

group = "com.numberone"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(VERSIONS.JAVA) // non-null
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
    }
}

dependencies {
    // ====================================== prod ======================================
    // auth
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt:0.12.3")

    // db
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("mysql:mysql-connector-java:${VERSIONS.MYSQL_CONNECTOR}")

    // web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${VERSIONS.SWAGGER}")

    // aws cloud
    implementation("io.awspring.cloud:spring-cloud-starter-aws-secrets-manager-config:${VERSIONS.AWS_SPRING_CLOUD}")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

    // logging
    implementation("io.github.microutils:kotlin-logging-jvm:${VERSIONS.KOTLIN_LOGGING}")

    // ====================================== test ======================================
    // security
    testImplementation("org.springframework.security:spring-security-test")

    // spring
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // junit
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // open feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // fcm
    implementation("com.google.firebase:firebase-admin:8.1.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
