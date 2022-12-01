object Versions {
    const val koin = "3.2.0"
    const val koinAndroidCompose = "3.3.0"
    const val koinAndroid = "3.3.0"

    const val kermit = "1.1.3"
    const val detekt = "1.22.0-RC3"
    const val compose_rules = "0.0.22"
    const val ktor = "2.1.2"
    const val sqlDelight = "1.5.4"
    const val kotlinCoroutines = "1.6.4"
    const val kotlinxSerialization = "1.3.3"
    const val kmpNativeCoroutinesVersion = "0.13.1"

    const val compose = "1.3.0"
    const val composeCompiler = "1.3.2"
    const val navCompose = "2.5.2"
    const val composeMaterial3 = "1.0.0"

    const val activityCompose = "1.6.0-rc02"
    const val lifecycleKtx = "2.6.0-alpha02"
    const val lifecycleRuntimeKtx = lifecycleKtx
    const val lifecycleViewmodelKtx = lifecycleKtx
}

object Deps {
    object Gradle {
        const val sqlDelight = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"
    }
    object Koin {
        const val core = "io.insert-koin:koin-core:${Versions.koin}"
        const val test = "io.insert-koin:koin-test:${Versions.koin}"
        const val android = "io.insert-koin:koin-android:${Versions.koinAndroid}"
        const val compose = "io.insert-koin:koin-androidx-compose:${Versions.koinAndroidCompose}"

    }

    object Kotlinx {
        const val serializationCore =
            "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerialization}"
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
        const val coroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}"
    }

    object Log {
        const val kermit = "co.touchlab:kermit:${Versions.kermit}"
    }

    object KtorClient {
        const val auth = "io.ktor:ktor-client-auth:${Versions.ktor}"
        const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val cio = "io.ktor:ktor-client-cio:${Versions.ktor}"
        const val serialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
        const val logging = "io.ktor:ktor-client-logging:${Versions.ktor}"
        const val android = "io.ktor:ktor-client-android:${Versions.ktor}"
        const val okhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
        const val darwin = "io.ktor:ktor-client-darwin:${Versions.ktor}"
    }

    object SqlDelight {
        const val runtime = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
        const val coroutineExtensions =
            "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
        const val androidDriver = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
        const val nativeDriver = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
    }

    object JetpackPreviews {
        // Lower-level APIs with support for custom serialization
        const val datasore_okio = "androidx.datastore:datastore-core-okio:1.1.0-dev01"
        // Higher-level APIs for storing values of basic types
        const val datasore = "androidx.datastore:datastore-preferences-core:1.1.0-dev01"
    }

    object Compose {
        const val compiler = "androidx.compose.compiler:compiler:${Versions.composeCompiler}"
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val uiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
        const val foundationLayout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
        const val material = "androidx.compose.material:material:${Versions.compose}"
        const val navigation = "androidx.navigation:navigation-compose:${Versions.navCompose}"
        const val coilCompose = "io.coil-kt:coil-compose:2.0.0"
    }


    object AndroidX {
        const val lifecycleRuntimeCompose = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycleRuntimeKtx}"
        const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"
        const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewmodelKtx}"

        const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
        const val material3 = "androidx.compose.material3:material3:${Versions.composeMaterial3}"
        const val material3WindowSizeClass = "androidx.compose.material3:material3-window-size-class:${Versions.composeMaterial3}"
        const val splashScreen = "androidx.core:core-splashscreen:1.0.0"
    }

}