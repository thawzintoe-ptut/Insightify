import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.ktlint.idea) apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jlleitschuh.gradle.ktlint-idea")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        verbose.set(true)
        android.set(true)
        outputToConsole.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(true)
        enableExperimentalRules.set(true)
        disabledRules.set(
            setOf(
                "final-newline",
                "no_wildcard_imports",
            ),
        )
        reporters {
            reporter(ReporterType.CHECKSTYLE)
        }
        kotlinScriptAdditionalPaths {
            include(fileTree("scripts/"))
        }
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
        debug.set(true)
    }
}
