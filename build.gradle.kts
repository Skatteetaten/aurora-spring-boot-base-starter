plugins {
    `java-library`
    kotlin("jvm") version "1.7.20"
    id("no.skatteetaten.gradle.aurora") version "4.5.10"
    id("io.spring.dependency-management") version "1.1.0"
}

aurora {
    useLibDefaults
    useSpringBoot
    useVersions

    features {
        auroraStarters = false
    }

    versions {
        javaSourceCompatibility = "1.8"
    }
}

dependencies {
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2021.0.5"))

    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("org.springframework.cloud:spring-cloud-sleuth-brave")
    implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
}

tasks.named<Jar>("jar") {
    archiveClassifier.set("")
}