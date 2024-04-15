plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.hilt.android)
	alias(libs.plugins.kotlinx.serialization)
}

android {
	namespace = "${libs.versions.nameSpace.get()}.network"
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
				"proguard-rules.pro"
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
		buildConfig = true
	}
}

dependencies {
	implementation(project(":core:common"))
	implementation(project(":core:data"))
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)

	// Hilt
	implementation(libs.hilt.android)
	ksp(libs.hilt.android.compiler)
	ksp(libs.hilt.compiler)

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

	// timber
	implementation(libs.timber)
}
