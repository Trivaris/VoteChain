import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    alias(libs.plugins.android.application) 
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler) 
    alias(libs.plugins.kotlin.multiplatform) 
    alias(libs.plugins.kotlin.serialization) 
    alias(libs.plugins.sqldelight.plugin) 
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
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)

                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlin.reflect)
                implementation(libs.kotlinx.serialization.json)

                implementation(libs.jai.imageio.core)

                implementation(libs.zxing.core)
                implementation(libs.zxing.javase)

                implementation(libs.sqldelight.runtime)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(compose.preview)

                implementation(libs.androidx.activity.compose)

                implementation(libs.zxing.android.embedded)
                implementation(libs.jai.imageio.core)

                implementation(libs.sqldelight.driver.android)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.sqldelight.driver.desktop)
            }
        }
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
    lint {
        checkReleaseBuilds = false
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

sqldelight {
    databases {
        create("BlockDatabase") {
            packageName.set("com.trivaris.votechain")
        }
    }
}