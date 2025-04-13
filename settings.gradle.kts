rootProject.name = "VoteChain"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
//        maven { url = uri("https://dl.cloudsmith.io/public/libp2p/jvm-libp2p/maven/") }
//        maven { url = uri("https://jitpack.io") }
//        maven { url = uri("https://artifacts.consensys.net/public/maven/maven/") }
        maven { url = uri("https://nexus.devsrsouza.com.br/repository/maven-public/") }
        mavenCentral()
    }
}

include(":composeApp")