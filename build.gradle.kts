plugins {
    `java-library`
    kotlin("jvm") version "1.7.20"
    id("no.skatteetaten.gradle.aurora") version "4.5.10"
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
    api("org.springframework.boot:spring-boot-starter-actuator") {
        isTransitive = true
    }
    api("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
}

tasks.named<Jar>("jar") {
    archiveClassifier.set("")
}