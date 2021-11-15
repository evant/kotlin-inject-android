plugins {
    id("kotlin-inject.android")
    id("kotlin-inject.detekt")
    id("kotlin-inject.publish")
}

dependencies {
    api(project(":activity"))
    api(project(":fragment-annotations"))
    api(libs.kotlin.inject.runtime)
    api(libs.androidx.fragment.core)
}