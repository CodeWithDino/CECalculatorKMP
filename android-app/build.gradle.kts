import org.bouncycastle.asn1.iana.IANAObjectIdentifiers.experimental

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "dev.dk.currency.exchange.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "dev.dk.currency.exchange.android"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    buildFeatures {
        viewBinding = true
    }
    lint {
        abortOnError = false
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    sourceSets {
        getByName("main") {
            java {
                srcDirs("src/main/java", "src/test")
            }
        }
    }
}

dependencies {
    val nav_version = "2.5.2"
    val compose_version = "1.3.0"

    implementation(project(":shared"))

    with(Deps.Koin) {
        implementation(core)
        implementation(android)
        implementation(compose)
    }

    with(Deps.AndroidX) {
        implementation(splashScreen)
        implementation(lifecycleRuntimeCompose)
        implementation(lifecycleRuntimeKtx)
        implementation(lifecycleViewModelKtx)
    }

    //compose
    with(Deps.Compose) {
        implementation(ui)
        implementation(uiTooling)
        implementation(uiToolingPreview)
        implementation(navigation)
    }

    implementation("androidx.compose.foundation:foundation:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.activity:activity-ktx:1.5.1")

    // Material Components
    implementation("com.google.android.material:material:1.6.1")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    // Compose Material Design
    implementation("androidx.compose.material:material:$compose_version")
    
    // Animations
    implementation("androidx.compose.animation:animation:$compose_version")

    // Tooling support (Previews, etc.)
    debugImplementation(Deps.Compose.uiTooling)
    implementation(Deps.Compose.uiToolingPreview)

    implementation("androidx.compose.ui:ui-util:$compose_version")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1") {
        // https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-debug#debug-agent-and-android
        exclude(group = "org.jetbrains.kotlinx",module = "kotlinx-coroutines-debug")
    }

}

