@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.12.1"
}

ext {
    set("PUBLISH_GROUP_ID", "dev.sasikanth")
    set("PUBLISH_ARTIFACT_ID", "fancy-switch")
    set("PUBLISH_VERSION", "1.0.0")
}

android {
    namespace = "dev.sasikanth.fancy.toggle"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0-alpha02"
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

apiValidation {
    ignoredClasses.add("dev.sasikanth.fancy.toggle.BuildConfig")
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.3")
    api("androidx.compose.ui:ui:1.3.3")
    api("androidx.compose.material3:material3:1.0.1")
    api("androidx.compose.animation:animation-graphics:1.3.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.3.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.3.3")
}

apply(from = "${rootProject.projectDir}/scripts/publish-module.gradle")
