import com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import java.lang.System.getenv
import org.gradle.api.JavaVersion.VERSION_11
import org.gradle.api.JavaVersion.VERSION_1_8
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintFormatTask

val buildTools = BuildUtils()
val branchName = buildTools.gitBranch()
group = "no.skatteetaten.aurora.springboot"
version = buildTools.calculateVersion(properties["version"] as String, branchName)

repositories {
    mavenCentral()
    jcenter()
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin(Plugins.kotlin_gradle, Versions.kotlin_version))
    }
}

plugins {
    base
    checkstyle
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
            jvmTarget = VERSION_1_8.toString()
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

            pom {
                name.set("Aurora Spring Boot Base Starter")
                description.set(
                    "This starter contains all the basic configuration needed to " +
                    "run a spring-boot application on Aurora OpenShift"
                )
                url.set("https://github.com/Skatteetaten/aurora-spring-boot-base-starter")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("bjartek")
                        name.set("Bjarte S. Karlsen")
                        email.set("bjarte.karlsen@skatteetaten.no")
                    }
                    developer {
                        id.set("mikand13")
                        name.set("Anders Mikkelsen")
                        email.set("anders.mikkelsen@skatteetaten.no")
                    }
                    developer {
                        id.set("jarlehansen")
                        name.set("Jarle Hansen")
                        email.set("jarle.hansen@skatteetaten.no")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Skatteetaten/aurora-spring-boot-base-starter.git")
                    developerConnection.set("scm:git:git@github.com/Skatteetaten/aurora-spring-boot-base-starter.git")
                    url.set("https://github.com/Skatteetaten/aurora-spring-boot-base-starter")
                }
            }

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
}

signing {
    @Suppress("UnstableApiUsage")
    useGpgCmd()

    @Suppress("UnstableApiUsage")
    sign(publishing.publications["mavenJava"])
}
