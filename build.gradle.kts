plugins {
	java
	id("org.springframework.boot") version "2.7.1"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "app"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

configurations.all {
	exclude(group = "ch.qos.logback", module = "logback-classic")
	exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
}


repositories {
	mavenCentral()
	// Add additional repositories if needed
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.18") {
		exclude(group = "ch.qos.logback", module = "logback-classic")
	}
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-security") {
		exclude(module = "spring-boot-starter-logging")
		exclude(module = "logback-classic")
		exclude(group = "ch.qos.logback", module = "logback-classic")
	}
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web") {
		exclude(group = "ch.qos.logback", module = "logback-classic")
	}
	implementation("io.jsonwebtoken:jjwt-api:0.12.5")
	implementation("com.nimbusds:nimbus-jose-jwt:9.38-rc3")
	implementation("org.springframework.boot:spring-boot-starter-aop:3.0.5")
	implementation("org.springframework.boot:spring-boot-starter")
//	implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1") {
//		exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
//	}
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	implementation("org.apache.spark:spark-core_2.13:3.5.0") {
		exclude(group = "javax.servlet", module = "javax.servlet-api")
		exclude(group = "org.glassfish", module = "javax.servlet")
		exclude(group = "org.eclipse.jetty", module = "javax.servlet")
	}
	//implementation("javax.servlet:javax.servlet-api:4.0.1") // Add the correct version
	//implementation("org.glassfish.jersey.core:jersey-client:2.36") // Add the correct version

	implementation("jakarta.servlet:jakarta.servlet-api:4.0.3")
	implementation("org.glassfish.jersey.core:jersey-client:3.1.2") {
		exclude(group = "org.glassfish.jersey.core", module = "jersey-server")
		exclude(group = "org.glassfish.jersey.containers", module = "jersey-container-servlet")
		exclude(group = "org.glassfish.jersey.containers", module = "jersey-container-servlet-core")
	}
	compileOnly("org.projectlombok:lombok")
	implementation("org.apache.spark:spark-sql_2.13:3.5.0") {
		exclude(group = "org.codehaus.janino", module = "janino")
		exclude(group = "org.codehaus.janino", module = "commons-compiler")
	}
	implementation("org.scala-lang:scala-library:2.13.10")
	implementation("org.msgpack:msgpack-core:0.9.3")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("org.codehaus.janino:commons-compiler:3.1.12")
	implementation("org.codehaus.janino:janino:3.1.12")
	implementation("org.slf4j:slf4j-api:1.7.32")
	implementation("org.apache.logging.log4j:log4j-core:2.17.2")
	implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.2")
}

tasks.withType<Test> {
	useJUnitPlatform()
}