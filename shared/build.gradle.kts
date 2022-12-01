plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
    id("com.rickclephas.kmp.nativecoroutines")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("io.github.luca992.multiplatform-swiftpackage") version "2.0.5-arm64"
}

// CocoaPods requires the podspec to have a version.
version = "1.0"
kotlin {
    android()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                with(Deps.KtorClient) {
                    implementation(auth)
                    implementation(core)
                    implementation(logging)
                    implementation(serialization)
                    implementation(contentNegotiation)
                }
                with(Deps.Log) {
                    implementation(kermit)
                }
                with(Deps.SqlDelight) {
                    implementation(runtime)
                    implementation(coroutineExtensions)
                }
                with(Deps.Kotlinx) {
                    implementation(coroutinesCore)
                    implementation(serializationCore)
                }
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                with(Deps.Koin) {
                    api(core)
                    api(test)
                }
                with(Deps.JetpackPreviews) {
                    implementation(datasore_okio)
                    implementation(datasore)
                }
            }
        }
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                with(Deps.KtorClient) {
                    implementation(android)
                    implementation(okhttp)
                }
                with(Deps.SqlDelight) {
                    implementation(androidDriver)
                }
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                with(Deps.KtorClient) {
                    implementation(darwin)
                }
                with(Deps.SqlDelight) {
                    implementation(nativeDriver)
                }
            }

        }

        val androidTest by getting
        val iosTest by creating
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting

        val commonTest by getting {
            dependencies {
                implementation(Deps.Koin.test)
                implementation(Deps.Kotlinx.coroutinesTest)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        androidTest.dependsOn(commonTest)
        iosTest.dependsOn(commonTest)
        iosX64Test.dependsOn(iosTest)
        iosArm64Test.dependsOn(iosTest)
        iosSimulatorArm64Test.dependsOn(iosTest)
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
}
dependencies {
    implementation("com.google.android.gms:play-services-base:18.0.1")
}

sqldelight {
    database("CECalculatorDatabase") {
        packageName = "dev.dk.currency.exchange.db"
        sourceFolders = listOf("sqldelight")
    }
}

multiplatformSwiftPackage {
    packageName("CECalculatorKit")
    swiftToolsVersion("5.3")
    targetPlatforms {
        iOS { v("14") }
    }
}