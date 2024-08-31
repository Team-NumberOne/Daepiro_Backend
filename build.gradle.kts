plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.numberone"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
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
	implementation("mysql:mysql-connector-java:8.0.26")

	// web
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// aws cloud
	implementation("io.awspring.cloud:spring-cloud-starter-aws-secrets-manager-config:2.4.4")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

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
