import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-inject.jvm")
    id("kotlin-inject.detekt")
    id("com.google.devtools.ksp")
    id("kotlin-inject.publish")
}

dependencies {
    implementation(project(":fragment-factory-compiler:core"))
    implementation(libs.kotlin.inject.compiler.ksp)
    implementation(libs.ksp)
    implementation(libs.kotlinpoet.ksp)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview"
}
