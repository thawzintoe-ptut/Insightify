pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Insightify"
include(":app")
include(":core")
include(":core:testing")
include(":core:ui")
include(":core:data")
include(":core:common")
include(":core:domain")
include(":feature")
include(":feature:auth")
include(":core:network")
include(":feature:home")
