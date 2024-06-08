buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.1")
        classpath ("com.android.tools.build:gradle:7.4.2")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.9.9")

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.android.application") apply false
    id("org.jetbrains.kotlin.android") apply false
    id("com.google.gms.google-services") version "4.3.1" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}