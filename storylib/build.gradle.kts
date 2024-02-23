import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.publishing

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("maven-publish")
}

android {
    namespace = "com.carb0rane.storylib"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

publishing{
    publications{
        create<MavenPublication>("Maven"){
            groupId = "com.carb0rane"
            artifactId = "storylib"
            version = "0.0.3-alpha"
            artifact("$buildDir/outputs/aar/storylib-release.aar")
        }
    }
    repositories{
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/carb0rane/storylib")
            credentials {
                username  = project.findProperty("githubUserName").toString()
                password =  project.findProperty("githubToken").toString()
            }
        }

    }
}

dependencies {

    //coil
    implementation("io.coil-kt:coil:2.5.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

}

