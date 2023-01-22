plugins {
    kotlin("android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.userAuthDomain))

    "implementation"(Coil.coilCompose)
    "implementation"(Coil.coilSvg)
//    "implementation" ("com.google.accompanist:accompanist-pager:0.23.1")

    "implementation"(platform("com.google.firebase:firebase-bom:30.3.1"))
    "implementation"("com.google.firebase:firebase-auth-ktx")
    "implementation"("com.google.firebase:firebase-core:21.1.1")
    "implementation"("com.google.firebase:firebase-crashlytics-ktx")
    "implementation"("com.google.firebase:firebase-analytics-ktx")
    "implementation"("com.google.firebase:firebase-firestore-ktx")
    "implementation"("com.google.firebase:firebase-perf-ktx")
    "implementation"("com.google.firebase:firebase-config-ktx")
    "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
}