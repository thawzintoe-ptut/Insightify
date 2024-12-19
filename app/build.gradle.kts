import org.jlleitschuh.gradle.ktlint.reporter.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ktlint)
}

android {
    namespace = libs.versions.nameSpace.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.nameSpace.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    lint {
        xmlReport = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        flavorDimensions += "app"
        productFlavors {
            create("demo") {
                dimension = "app"
                applicationIdSuffix = ".demo"
                versionNameSuffix = "-demo"
                buildConfigField(
                    "String",
                    "API_BASE_URL",
                    "\"https://nimble-survey-web-mock.fly.dev\"",
                )
                buildConfigField(
                    "String",
                    "CLIENT_ID",
                    "\"6GbE8dhoz519l2N_F99StqoOs6Tcmm1rXgda4q__rIw\"",
                )
                buildConfigField(
                    "String",
                    "CLIENT_SECRET",
                    "\"_ayfIm7BeUAhx2W1OUqi20fwO3uNxfo1QstyKlFCgHw\"",
                )
            }
            create("uat") {
                dimension = "app"
                applicationIdSuffix = ".uat"
                versionNameSuffix = "-uat"
                buildConfigField(
                    "String",
                    "API_BASE_URL",
                    "\"https://survey-api-staging.nimblehq.co/api/v1\"",
                )
                buildConfigField(
                    "String",
                    "CLIENT_ID",
                    "\"6GbE8dhoz519l2N_F99StqoOs6Tcmm1rXgda4q__rIw\"",
                )
                buildConfigField(
                    "String",
                    "CLIENT_SECRET",
                    "\"_ayfIm7BeUAhx2W1OUqi20fwO3uNxfo1QstyKlFCgHw\"",
                )
            }
            create("prod") {
                dimension = "app"
                applicationIdSuffix = ".full"
                versionNameSuffix = "-full"
                buildConfigField(
                    "String",
                    "API_BASE_URL",
                    "\"https://survey-api.nimblehq.co/api/v1\"",
                )
                buildConfigField(
                    "String",
                    "CLIENT_ID",
                    "\"6GbE8dhoz519l2N_F99StqoOs6Tcmm1rXgda4q__rIw\"",
                )
                buildConfigField(
                    "String",
                    "CLIENT_SECRET",
                    "\"_ayfIm7BeUAhx2W1OUqi20fwO3uNxfo1QstyKlFCgHw\"",
                )
            }
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
        buildConfig = true
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
    implementation(project(":core:ui"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation)
    implementation(libs.accompanist.systemUiController)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest) // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)

    // paging
    implementation(libs.androidx.paging)

    // KotlinX
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.datetime)

    // Retrofit
    implementation(libs.retrofit.retrofit)
    implementation(libs.retrofit.converter.json)
    implementation(libs.retrofit.interceptor)

    // OkHttp
    api(libs.okhttp)

    // Chucker
    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.no.op)

    // SharedPref
    implementation(libs.preference.ktx)
    implementation(libs.security.crypto)

    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)

    // firebase
}
