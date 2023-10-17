apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.userAuthPresentation))
    "implementation"(project(Modules.workoutLoggerDomain))

    "implementation"(Coil.coilCompose)
    "implementation"(Coil.coilSvg)
    "implementation"("co.yml:ycharts:2.1.0")
}