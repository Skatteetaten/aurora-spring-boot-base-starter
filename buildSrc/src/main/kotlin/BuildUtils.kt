import org.gradle.api.publish.VersionMappingStrategy
import org.gradle.api.publish.maven.MavenPom

class BuildUtils {
    fun calculateVersion(version: String, branch: String): String = when {
        branch.isBlank() -> version
        branch == "develop" -> "$version-SNAPSHOT"
        branch == "master" -> version
        branch.startsWith("release", ignoreCase = true) -> version
        else -> "$version-$branch"
    }

    fun gitBranch(): String =
        System.getenv("BRANCH_NAME") ?: Runtime.getRuntime().exec("git rev-parse --abbrev-ref HEAD")
            .inputStream
            .bufferedReader()
            .readText()
            .trim()
            .replace('/', '-')

    fun pom(): (MavenPom).() -> Unit = {
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

    fun versionMapping(): (VersionMappingStrategy).() -> Unit = {
        usage("java-api") {
            fromResolutionOf("runtimeClasspath")
        }
        usage("java-runtime") {
            fromResolutionResult()
        }
    }
}