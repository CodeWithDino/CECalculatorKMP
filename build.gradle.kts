plugins {
    kotlin("multiplatform").version("1.7.20").apply(false)
    kotlin("plugin.serialization").version("1.7.20").apply(false)
    id("com.android.library").version("7.3.0").apply(false)
    id("com.squareup.sqldelight").version("1.5.4").apply(false)
}

buildscript {
    val kotlinVersion: String by project
    println(kotlinVersion)

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("com.rickclephas.kmp:kmp-nativecoroutines-gradle-plugin:${Versions.kmpNativeCoroutinesVersion}")
        with(Deps.Gradle) {
            classpath(sqlDelight)
        }
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven {setUrl("https://jitpack.io")}

    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}