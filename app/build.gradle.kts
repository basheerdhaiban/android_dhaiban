plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.kotlin.android)
//    kotlin("kapt")
    alias(libs.plugins.ksp)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"

    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
}

android {
    namespace = "com.semicolon.dhaiban"
    compileSdk = libs.versions.compile.sdk.version.get().toInt()

    defaultConfig {
        applicationId = "com.semicolon.dhaiban"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

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
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
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
//    kapt {
//        generateStubs = true
//    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.1"
//    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
// Ensure toolchains don’t silently pick JDK 23
//java {
//    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(23))
//    }
//}
//kotlin {
//    jvmToolchain(23)
//}
dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.coroutines.android)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.common)

    implementation(libs.play.services.location)
    ksp(libs.hilt.ksp)
    implementation(libs.bundles.voyager)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.koin.compose)
    implementation(libs.kotlin.date.time)
    implementation(libs.koin.android)
    implementation(libs.koin.insert)
    implementation(libs.compose.materialIcons)
    implementation(libs.ktor.client)
    implementation(libs.ktor.cio.engine)
    implementation(libs.ktor.loging)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.json.serialization)
    implementation(libs.ktor.gson)
//    implementation(libs.ktor.client.serialization)
//    implementation(libs.ktor.serialization.json)
//    implementation(libs.ktor.client.json)

    implementation(libs.coil)
    implementation(libs.jsoup)


    implementation(libs.maps.compose)
    // room
//    kapt(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.room)

    implementation(libs.accompanist.permissions)
    implementation(libs.swipe)

    implementation(libs.androidx.material)

    //firebase
    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.analytics)
    implementation(libs.google.firebase.messaging)
    // mobile country Key
    implementation(libs.country.picker)
    // mobile number validation
    implementation(libs.libphonenumber)
    implementation(libs.compose.materialIcons)

}