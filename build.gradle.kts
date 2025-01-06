buildscript {

    val kotlinVersion by extra { "1.8.21" }
    val r8Tools = "r8:8.2.16-dev"
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://storage.googleapis.com/r8-releases/raw")
        }
    }

    dependencies {
        classpath(kotlin("serialization", version = "1.9.10"))
        classpath("com.android.tools:${r8Tools}")
        classpath(libs.jetbrains.kotlin.gradle)
        classpath(libs.androidx.navigation)
        classpath(libs.google.dagger.hilt.plugin)
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}