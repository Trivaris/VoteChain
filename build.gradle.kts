val logbackVersion: String by project
val kotlinVersion: String by project
val ktorVersion: String by project
val yass2Version: String by project
val jsonVersion: String by project
val janinoVersion: String by project
val wireguardVersion: String by project

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.ktor.plugin") version "3.0.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    application
}

application {
    mainClass.set("com.trivaris.votechain.Mainkt")
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "io.ktor.plugin")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    dependencies {
        implementation(project(":"))
    }
}

allprojects {
    group = "com.trivaris"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    application {
        mainClass.set("io.ktor.server.netty.EngineMain")

        val isDevelopment: Boolean = project.ext.has("development")
        applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
    }

    dependencies {
        implementation("io.ktor:ktor-server-core-jvm:${ktorVersion}")
        implementation("io.ktor:ktor-server-host-common-jvm:${ktorVersion}")
        implementation("io.ktor:ktor-server-status-pages-jvm:${ktorVersion}")
        implementation("io.ktor:ktor-server-default-headers-jvm:${ktorVersion}")
        implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
        implementation("io.ktor:ktor-server-netty-jvm:${ktorVersion}")
        implementation("io.ktor:ktor-client-core:${ktorVersion}")
        implementation("io.ktor:ktor-client-cio:${ktorVersion}")
        implementation("io.ktor:ktor-client-serialization:${ktorVersion}")
        implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
        implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
        implementation("io.ktor:ktor-server-websockets:$ktorVersion")
        implementation("org.json:json:${jsonVersion}")

        implementation("ch.softappeal.yass2:yass2-ktor:${yass2Version}")
        implementation("ch.qos.logback:logback-classic:$logbackVersion")
        implementation("org.codehaus.janino:janino:$janinoVersion")
        //implementation("com.wireguard.android:tunnel:$wireguardVersion")

        testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
        testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    }
}