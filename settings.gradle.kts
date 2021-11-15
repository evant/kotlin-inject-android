enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        mavenCentral()
        google()
    }
}

rootProject.name = "kotlin-inject"

include(":activity")
include(":fragment")
include(":fragment-annotations")
include(":fragment-factory-compiler:core")
include(":fragment-factory-compiler:ksp")
include(":fragment-factory-compiler:kapt")
include(":integration-tests:kapt")
include(":integration-tests:ksp")
