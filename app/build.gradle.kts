plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0"
    id("androidx.room") version "2.7.2" apply false
}

android {
    namespace = "net.osdn.ja.gokigen.wearos.calendar"
    compileSdk = 36

    defaultConfig {
        applicationId = "net.osdn.ja.gokigen.wearos.calendar"
        minSdk = 26
        targetSdk = 36
        versionCode = 100110
        versionName = "1.1.10"
        vectorDrawables {
            useSupportLibrary = true
        }
        //javaCompileOptions {
        //    annotationProcessorOptions {
        //        arguments += mapOf(
        //            "room.schemaLocation" to "$projectDir/schemas",
        //            "room.incremental" to "true"
        //        )
        //    }
        //}
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
    //composeOptions {
    //    kotlinCompilerExtensionVersion = "1.5.3"
    //}
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.core:core-ktx:1.16.0")
    //implementation("androidx.percentlayout:percentlayout:1.0.0")
    //implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.2")

    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.wear:wear-tooling-preview:1.0.0")

    val composeVersion = "2025.07.00"
    implementation(platform("androidx.compose:compose-bom:$composeVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")

    val wearComposeVersion = "1.4.1"
    implementation("androidx.wear.compose:compose-foundation:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-material:$wearComposeVersion")

    val tilesVersion = "1.5.0"
    implementation("androidx.wear.tiles:tiles:$tilesVersion")
    implementation("androidx.wear.tiles:tiles-material:$tilesVersion")

    implementation("com.google.android.horologist:horologist-compose-tools:0.6.23")
    implementation("com.google.android.horologist:horologist-tiles:0.7.15")

    val roomVersion = "2.7.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    //annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
}
