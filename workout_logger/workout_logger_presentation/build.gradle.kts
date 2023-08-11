apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.workoutLoggerDomain))
    "implementation"(project(Modules.userAuthPresentation))

    "implementation"(Coil.coilCompose)
    "implementation"(Coil.coilSvg)
    "implementation" ("com.google.accompanist:accompanist-pager:0.23.1")
    "implementation"("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha03")
    "implementation"("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    "implementation"("com.himanshoe:kalendar:1.3.2")
}