apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreUi))
//    "implementation"(project(Modules.workoutLoggerDomain))

    "implementation"(Coil.coilCompose)
    "implementation"(Coil.coilSvg)
    "implementation"("androidx.wear:wear-remote-interactions:1.0.0")
    "implementation"("com.google.android.gms:play-services-wearable:18.0.0")
    "implementation"(Compose.activityCompose)
    "implementation"("androidx.appcompat:appcompat:1.6.1")
    "implementation"("com.google.accompanist:accompanist-systemuicontroller:0.17.0")
}