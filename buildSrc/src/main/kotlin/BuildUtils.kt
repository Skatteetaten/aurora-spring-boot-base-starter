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
}