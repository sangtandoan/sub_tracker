buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.9.1")
    }
}

plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "11.9.1"
}

group = "com.sangtandoan"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
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
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // https://mvnrepository.com/artifact/org.mapstruct/mapstruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    // https://mvnrepository.com/artifact/org.mapstruct/mapstruct-processor
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
    implementation("org.springframework.boot:spring-boot-starter-validation:3.5.0")
    // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-oauth2-client
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client:3.5.0")
    // https://mvnrepository.com/artifact/com.google.oauth-client/google-oauth-client-jetty
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.39.0")
    // https://mvnrepository.com/artifact/com.google.api-client/google-api-client
    implementation("com.google.api-client:google-api-client:2.8.0")
    // https://mvnrepository.com/artifact/io.hypersistence/hypersistence-utils-hibernate-63
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.10.1")
    annotationProcessor("org.hibernate:hibernate-jpamodelgen:6.3.0.Final")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-mail
    implementation("org.springframework.boot:spring-boot-starter-mail:3.5.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

flyway {
    url = "${System.getenv("SPRING_DATASOURCE_URL")}"
    user = "${System.getenv("SPRING_DATASOURCE_USERNAME")}"
    password = "${System.getenv("SPRING_DATASOURCE_PASSWORD")}"
    cleanDisabled = false
}
