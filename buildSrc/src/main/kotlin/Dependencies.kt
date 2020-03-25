object Versions {
    const val spring_dependency_management_plugin_version = "1.0.9.RELEASE"
    const val spring_boot_plugin_version = "2.2.5.RELEASE"
    const val gradle_versions_plugin_version = "0.28.0"
    const val gradle_ktlint_plugin_version = "9.2.1"
    const val test_logger_plugin_version = "2.0.0"
    const val nexus_staging_plugin_version = "0.21.2"
    const val maven_settings_plugin_version = "0.5"
    const val kotlin_version = "1.3.71"
    const val kotlinx_version = "1.3.4"
    const val kotlin_logging_version = "1.7.9"
    const val junit_version = "5.6.1"
    const val assertk_version = "0.22"
    const val puppycrawl_version = "8.30"
    const val skatteetaten_checkstyle_config_version = "2.2.4"
}

object Plugins {
    const val idea = "idea"
    const val java = "java"
    const val jvm = "jvm"
    const val kapt = "kapt"
    const val kotlin_gradle = "gradle-plugin"
    const val spring_dependency_management = "io.spring.dependency-management"
    const val spring_boot = "org.springframework.boot"
    const val gradle_versions = "com.github.ben-manes.versions"
    const val ktlint = "org.jlleitschuh.gradle.ktlint"
    const val jetbrains_kotlin_spring = "org.jetbrains.kotlin.plugin.spring"
    const val test_logger = "com.adarshr.test-logger"
    const val nexus_staging = "io.codearte.nexus-staging"
    const val maven_settings = "net.linguica.maven-settings"
}

object Libs {
    // Spring boot
    const val spring_boot_actuator_starter = "org.springframework.boot:spring-boot-starter-actuator"

    // Spring boot test
    const val spring_boot_test_starter = "org.springframework.boot:spring-boot-starter-test"

    // Prometheus
    const val micrometer_prometheus = "io.micrometer:micrometer-registry-prometheus"

    // Kotlin
    const val kotlin_stdlib = "stdlib-jdk8"
    const val kotlinx_coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx_version}"

    // Logging
    const val kotlin_logging = "io.github.microutils:kotlin-logging:${Versions.kotlin_logging_version}"

    // Junit
    const val junit = "org.junit.jupiter:junit-jupiter:${Versions.junit_version}"
    const val assertk = "com.willowtreeapps.assertk:assertk-jvm:${Versions.assertk_version}"

    // Checkstyle
    const val puppycrawl_checkstyle = "com.puppycrawl.tools:checkstyle:${Versions.puppycrawl_version}"
    const val skatteetaten_checkstyle_config = "no.skatteetaten.aurora.checkstyle:checkstyle-config:${Versions.skatteetaten_checkstyle_config_version}"
}