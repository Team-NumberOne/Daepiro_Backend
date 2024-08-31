plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.25"
}

// 버전 관리
val versions = mapOf(
    "mysqlConnector" to "8.0.26",
    "awsSpringCloud" to "2.4.4",
    "java" to "17",
    "kotlinLogging" to "3.0.5",
    "swagger" to "2.2.0"
)

group = "com.numberone"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(versions["java"]!!) // non-null
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

dependencies {
    // ====================================== prod ======================================
    // security
    //implementation("org.springframework.boot:spring-boot-starter-security") todo 시큐리티 적용

    // db
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("mysql:mysql-connector-java:${versions["mysqlConnector"]}")

    // web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // swagger
    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:${versions["swagger"]}")

    // aws cloud
    implementation("io.awspring.cloud:spring-cloud-starter-aws-secrets-manager-config:${versions["awsSpringCloud"]}")

    // logging
    implementation("io.github.microutils:kotlin-logging-jvm:${versions["kotlinLogging"]}")

    // ====================================== test ======================================
    // security
    testImplementation("org.springframework.security:spring-security-test")

    // spring
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // junit
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}