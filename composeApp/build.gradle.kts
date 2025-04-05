import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

val localProperties = Properties().apply {
    file("../local.properties").let {
        if (it.exists()) {
            load(it.inputStream())
        }
    }
}


val passwordStore: String = localProperties.getProperty("passwordStore")
val aliasKey: String = localProperties.getProperty("aliasKey")
val passwordKey: String = localProperties.getProperty("passwordKey")

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.google.zxing.embedded)
            implementation(libs.jai.imageio.core)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.stdlib)

            implementation(libs.jai.imageio.core)
            implementation(libs.kotlinx.serialization)
            implementation(libs.google.zxing.core)
            implementation(libs.google.zxing.javase)

            implementation(libs.kotlin.reflect)

            implementation(libs.exposed.core)
            implementation(libs.exposed.dao)
            implementation(libs.exposed.jdbc)
            implementation(libs.sqlite.jdbc)
            implementation("org.sqldroid:sqldroid:1.0.3") {
                exclude(group = "org.xerial", module = "sqlite-jdbc")
            }
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
    sourceSets.commonMain.dependencies {
        implementation(kotlin("reflect"))
    }
}

android {
    namespace = "com.trivaris.votechain.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.trivaris.votechain.app"
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
    signingConfigs {
        getByName("debug") {
            storeFile = file("keystore.jks")
            storePassword = passwordStore
            keyAlias = aliasKey
            keyPassword = passwordKey
        }
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = passwordStore
            keyAlias = aliasKey
            keyPassword = passwordKey
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.trivaris.votechain.app.MainKt"

        nativeDistributions {
            windows {
                includeAllModules = true
                iconFile.set(project.file("src\\resources\\icon.png"))
            }
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "VoteChain"
            packageVersion = "1.0.0"
            buildTypes.release.proguard {
                isEnabled.set(false)
            }
        }

    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.trivaris.votechain.resources"
    generateResClass = auto
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.trivaris.votechain.app.MainKt"
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}