import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.version

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url=uri("https://jitpack.io") }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    dependencies {
        classpath(libs.secrets.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.compose.compiler) apply false

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    id("com.google.gms.google-services") version "4.4.3" apply false
    id("com.google.firebase.crashlytics") version "3.0.6" apply false
}