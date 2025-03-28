import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kover)
}

android {
    namespace = "${libs.versions.nameSpace.get()}.auth"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

ktlint {
    android.set(false)
    ignoreFailures.set(false)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
    filter {
        exclude("**/style-violations.kt")
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

dependencies {
    kover(project(":app"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))

    // test
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.turbine)
    testImplementation(libs.assertk)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockk)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraint.compose)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit) // navigation
    implementation(libs.androidx.navigation)
    androidTestImplementation(libs.androidx.espresso.core) // lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx) // compose bom
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // androidx compose ui
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3) // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler) // Lifecycle
    implementation(libs.lifecycle.viewmodel.scope)
    implementation(libs.lifecycle.compiler)
    implementation(libs.lifecycle.runtime.compose) // kotlinx serialization
    implementation(libs.kotlinx.serialization)
    // coil
    implementation(libs.coil)
}
