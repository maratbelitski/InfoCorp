buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.1")
        classpath ("com.android.tools.build:gradle:7.4.2")

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") apply false
    id("org.jetbrains.kotlin.android") apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.gms.google-services") version "4.3.1" apply false
}