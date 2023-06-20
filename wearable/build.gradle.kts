plugins {
    id("com.android.application")
    kotlin("android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.hbaez.workoutbuddy"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.hbaez.workoutbuddy"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeCompilerVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(Compose.compiler)
    implementation(Compose.ui)
    implementation(Compose.uiToolingPreview)
    implementation(Compose.hiltNavigationCompose)
    implementation(Compose.material)
    implementation(Compose.materialExtended)
    implementation(Compose.runtime)
    implementation("androidx.wear.compose:compose-navigation:1.1.0")
    implementation(Compose.viewModelCompose)
    implementation(Compose.activityCompose)

    implementation(DaggerHilt.hiltAndroid)
    kapt(DaggerHilt.hiltCompiler)

    implementation(project(Modules.core))
    implementation(project(Modules.coreUi))
    implementation(project(Modules.userAuthPresentation))

    implementation(platform("com.google.firebase:firebase-bom:30.3.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.firebase:firebase-config-ktx")

    "implementation"("androidx.wear:wear-remote-interactions:1.0.0")
    "implementation"("com.google.android.gms:play-services-wearable:18.0.0")
    "implementation"("androidx.wear:wear-phone-interactions:1.0.1")
    "implementation"("com.google.accompanist:accompanist-systemuicontroller:0.17.0")
    "implementation"("androidx.wear.compose:compose-material:1.1.2")
    "implementation"("androidx.wear.compose:compose-foundation:1.1.2")
    "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.9")

    implementation("androidx.wear:wear-input:1.2.0-alpha02")

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)

    implementation(Coil.coilCompose)
    implementation(Coil.coilSvg)

    implementation(Google.material)

    implementation(Retrofit.okHttp)
    implementation(Retrofit.retrofit)
    implementation(Retrofit.okHttpLoggingInterceptor)
    implementation(Retrofit.moshiConverter)

    kapt(Room.roomCompiler)
    implementation(Room.roomKtx)
    implementation(Room.roomRuntime)

    implementation(Coroutines.coroutines)

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha03")
}