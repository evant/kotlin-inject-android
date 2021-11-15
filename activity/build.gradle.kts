plugins {
    id("kotlin-inject.android")
    id("kotlin-inject.detekt")
    id("kotlin-inject.publish")
}

dependencies {
    api(libs.androidx.activity)
}