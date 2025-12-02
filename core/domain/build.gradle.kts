plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_23
    targetCompatibility = JavaVersion.VERSION_23
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_23
    }
}
dependencies {
    implementation (libs.dagger.android)
    implementation(libs.coroutines.android)
    implementation(libs.androidx.paging.common)
}