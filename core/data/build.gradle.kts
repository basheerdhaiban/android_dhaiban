plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
//    kotlin("kapt")
    alias(libs.plugins.ksp)
    kotlin("plugin.serialization") version "2.2.10"
}

android {
    namespace = "com.semicolon.data"
    compileSdk = libs.versions.compile.sdk.version.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_23
        targetCompatibility = JavaVersion.VERSION_23
    }
    kotlinOptions {
        jvmTarget = "23"
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.datastore)
    implementation(libs.kotlinx.json.serialization)
    implementation(libs.ktor.client)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.cio.engine)
    implementation(libs.ktor.loging)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.json.serialization)
    implementation(libs.ktor.gson)
    implementation(libs.koin.android)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.paging.common)

    // room
//    kapt(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.room)
//    implementation(compose.materialIconsExtended)
}