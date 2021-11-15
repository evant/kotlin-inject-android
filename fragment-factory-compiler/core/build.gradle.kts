plugins {
    id("kotlin-inject.jvm")
    id("kotlin-inject.detekt")
    id("kotlin-inject.publish")
}

dependencies {
    api(libs.kotlin.inject.compiler.core)
    api(libs.kotlinpoet)
}