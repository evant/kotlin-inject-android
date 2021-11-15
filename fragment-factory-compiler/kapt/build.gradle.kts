plugins {
    id("kotlin-inject.jvm")
    id("kotlin-inject.detekt")
    id("kotlin-inject.publish")
}

dependencies {
    implementation(project(":fragment-annotations"))
    implementation(project(":fragment-factory-compiler:core"))
    implementation(libs.kotlin.inject.runtime)
    implementation(libs.kotlin.inject.compiler.kapt)
    implementation(libs.kotlinx.metadata.jvm)
    compileOnly(libs.jdk.compiler)
}