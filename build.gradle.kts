import no.skatteetaten.aurora.gradle.plugins.extensions.implementationTransitiveNexusIQIssue

plugins {
    `java-library`
    kotlin("jvm") version "1.7.10"
    id("no.skatteetaten.gradle.aurora") version "4.5.8"
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
    implementationTransitiveNexusIQIssue("org.yaml:snakeyaml:1.33")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
}

tasks.named<Jar>("jar") {
    archiveClassifier.set("")
}