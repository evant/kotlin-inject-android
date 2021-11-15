plugins {
    id("kotlin-inject.android")
    id("kotlin-inject.detekt")
    id("kotlin-inject.merge-tests")
    id("com.google.devtools.ksp")
}

android {

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("../common/src/main/AndroidManifest.xml")
            kotlin.srcDir("../common/src/main/kotlin")
        }
        getByName("test") {
            kotlin.srcDir("../common/src/test/kotlin")
        }
    }
}

dependencies {
    implementation(project(":activity"))
    implementation(project(":fragment"))
    implementation(libs.androidx.fragment.test)

    ksp(libs.kotlin.inject.compiler.ksp)
    ksp(project(":fragment-factory-compiler:ksp"))

    testImplementation(libs.robolectric)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.bundles.android.test)
    testImplementation(libs.assertk)
}