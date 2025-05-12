import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.realm.plugin)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    /* listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    } */

    jvm("desktop")

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        moduleName = "composeApp"
//        browser {
//            val rootDirPath = project.rootDir.path
//            val projectDirPath = project.projectDir.path
//            commonWebpackConfig {
//                outputFileName = "composeApp.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    static = (static ?: mutableListOf()).apply {
//                        add(rootDirPath)
//                        add(projectDirPath)
//                    }
//                }
//            }
//        }
//        binaries.executable()
//    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                // Compose MPP
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                // Core MPP libs
                implementation(libs.kotlin.serialization)
                implementation(libs.stately.common)

                // Networking & RPC (common)
                implementation(libs.ktor.client.core)
                implementation(libs.yass.core)
                implementation(libs.yass.coroutines)
                implementation(libs.yass.ktor)

                // Navigator MPP
                implementation(libs.navigator)
                implementation(libs.navigator.screen.model)
                implementation(libs.navigator.transitions)
                implementation(libs.navigator.tabnavigator)
            }
        }

        val androidMain by getting {
            dependencies {
                // Android UI & lifecycle
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)

                // Koin on Android
                implementation(libs.koin.android)
                implementation(libs.koin.androidx)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)

                // Persistence
                implementation(libs.mongodb.realm)
                implementation(libs.datastore)
                implementation(libs.datastore.preferences)

                // Ktor engines
                implementation(libs.ktor.client.cio)

                // Server (optional)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.netty)

                // Voyager-Koin integration
                implementation(libs.navigator.koin)
            }
        }

        val desktopMain by getting {
            dependencies {
                // Desktop UI
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)

                // JVM engines & DI
                implementation(libs.ktor.client.cio)
                implementation(libs.navigator.koin)
                implementation(libs.koin.compose)
                implementation(libs.mongodb.realm)
            }
        }

//        val wasmJsMain by getting {
//            dependencies {
//                // JS-only Navigator & RPC
//                implementation(libs.navigator.js)
//                implementation(libs.yass.ktor.js)
//
//                // JS client engine
//                implementation(libs.ktor.client.js)
//            }
//        }
    }
}

android {
    namespace = "com.trivaris.votechain"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.trivaris.votechain"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.trivaris.votechain.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.trivaris.votechain"
            packageVersion = "1.0.0"
        }
    }
}

System.setProperty("otel.traces.exporter", "none")
System.setProperty("otel.metrics.exporter", "none")