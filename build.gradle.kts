import com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import java.lang.System.getenv
import org.gradle.api.JavaVersion.VERSION_11
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintFormatTask

val buildTools = BuildUtils()
val branchName = buildTools.gitBranch()
group = "no.skatteetaten.aurora.springboot"
version = buildTools.calculateVersion(properties["version"] as String, branchName)

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin(Plugins.kotlin_gradle, Versions.kotlin_version))
    }
}

repositories {
    mavenCentral()
    jcenter()
}

plugins {
    base
    checkstyle
    maven
    `maven-publish`
    signing

    id(Plugins.java)
    id(Plugins.idea)
    id(Plugins.spring_dependency_management) version Versions.spring_dependency_management_plugin_version
    id(Plugins.gradle_versions) version Versions.gradle_versions_plugin_version
    id(Plugins.spring_boot) version Versions.spring_boot_plugin_version
    id(Plugins.ktlint) version Versions.gradle_ktlint_plugin_version
    id(Plugins.jetbrains_kotlin_spring) version Versions.kotlin_version
    id(Plugins.test_logger) version Versions.test_logger_plugin_version
    id(Plugins.nexus_staging) version Versions.nexus_staging_plugin_version
    id(Plugins.maven_settings) version(Versions.maven_settings_plugin_version)

    kotlin(Plugins.jvm) version(Versions.kotlin_version)
}

val checkstyleRules = configurations.create("checkstyleRules")

dependencies {
    // Spring boot
    api(Libs.spring_boot_actuator_starter)

    // Prometheus
    api(Libs.micrometer_prometheus)

    // Checkstyle
    checkstyle(Libs.puppycrawl_checkstyle)
    checkstyleRules(Libs.skatteetaten_checkstyle_config)

    // Kotlin
    testImplementation(kotlin(Libs.kotlin_stdlib, Versions.kotlin_version))
    testImplementation(Libs.kotlinx_coroutines)
    testImplementation(Libs.kotlin_logging)

    // Junit
    testImplementation(Libs.junit)
    testImplementation(Libs.assertk)
    testImplementation(Libs.spring_boot_test_starter) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.version?.endsWith("-SNAPSHOT", true) == true && branchName == "master") {
            when (requested.version) {
                null -> throw GradleException("Null version is not valid on: ${requested.group}:${requested.name}")
                else -> throw GradleException("Found invalid version on: ${requested.group}:${requested.name}")
            }
        }
    }
}

java {
    @Suppress("UnstableApiUsage")
    withSourcesJar()
    @Suppress("UnstableApiUsage")
    withJavadocJar()
    sourceCompatibility = VERSION_11
}

checkstyle {
    config = project.resources.text.fromArchiveEntry(checkstyleRules, "checkstyle/checkstyle-with-metrics.xml")
    maxErrors = 0
}

testlogger {
    theme = PLAIN
}

springBoot {
    mainClassName = ""
}

tasks {
    val ktlintKotlinScriptFormat by existing(KtlintFormatTask::class)
    val ktlintFormat by existing(Task::class)

    wrapper {
        distributionType = ALL
    }

    withType<Test> {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        dependsOn(listOf(ktlintKotlinScriptFormat, ktlintFormat))

        kotlinOptions {
            jvmTarget = VERSION_11.toString()
        }
    }

    withType<JavaCompile>().configureEach {
        options.compilerArgs = listOf("-Xdoclint:none", "-Xlint:none", "-nowarn")
    }
}

nexusStaging {
    packageGroup = group as String
}

publishing {
    publications {
        repositories {
            mavenLocal()

            if (branchName == "develop" && getenv("CENTRAL") == "true") {
                maven(url = "https://oss.sonatype.org/content/repositories/snapshots/") {
                    name = "ossrh"
                }
            } else if (branchName == "master" && getenv("CENTRAL") == "true") {
                maven(url = "https://oss.sonatype.org/service/local/staging/deploy/maven2/") {
                    name = "ossrh"
                }
            }
        }

        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom(buildTools.pom())
            versionMapping(buildTools.versionMapping())
        }
    }
}

signing {
    @Suppress("UnstableApiUsage")
    useGpgCmd()

    @Suppress("UnstableApiUsage")
    sign(publishing.publications["mavenJava"])
}
