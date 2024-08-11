buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.2" apply false
    val kotlinVersion = "2.0.0"
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.compose") version kotlinVersion apply false

    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.2" apply false

    id("com.google.devtools.ksp") version "2.0.0-1.0.23" apply false
}