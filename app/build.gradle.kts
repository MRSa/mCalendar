plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "net.osdn.ja.gokigen.wearos.calendar"
    compileSdk = 34

    defaultConfig {
        applicationId = "net.osdn.ja.gokigen.wearos.calendar"
        minSdk = 26
        targetSdk = 33
        versionCode = 100001
        versionName = "1.0.1"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("com.google.android.gms:play-services-wearable:18.0.0")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    val composeVersion = "2023.08.00"
    implementation(platform("androidx.compose:compose-bom:$composeVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    val wearComposeVersion = "1.2.0"
    implementation("androidx.wear.compose:compose-material:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-foundation:$wearComposeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    val tilesVersion = "1.2.0"
    implementation("androidx.wear.tiles:tiles:$tilesVersion")
    implementation("androidx.wear.tiles:tiles-material:$tilesVersion")
    val horologistVersion = "0.5.3"
    implementation("com.google.android.horologist:horologist-compose-tools:$horologistVersion")
    implementation("com.google.android.horologist:horologist-tiles:$horologistVersion")
    implementation("androidx.wear.watchface:watchface-complications-data-source-ktx:1.1.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
