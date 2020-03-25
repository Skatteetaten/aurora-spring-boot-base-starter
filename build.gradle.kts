plugins {
    java

    kotlin("jvm") version "1.3.70"
    id("com.adarshr.test-logger") version "2.0.0"
}

group = "no.skatteetaten.aurora.springboot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.2.5.RELEASE")

    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.1")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.22")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.2.5.RELEASE") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}

testlogger {
    theme = com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
}