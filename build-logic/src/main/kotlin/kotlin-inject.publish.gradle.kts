import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.AndroidSourceDirectorySet

plugins {
    `maven-publish`
    signing
}

fun MavenPublication.mavenCentralPom() {
    groupId = rootProject.group.toString()
    version = rootProject.version.toString()

    // We want the artifactId to represent the full project path
    artifactId = path
        .removePrefix(":")
        .replace(":", "-")

    pom {
        name.set("kotlin-inject")
        description.set("A compile-time dependency injection library for kotlin")
        url.set("https://github.com/evant/kotlin-inject")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("evant")
                name.set("Evan Tatarka")
            }
        }
        scm {
            connection.set("https://github.com/evant/kotlin-inject.git")
            developerConnection.set("https://github.com/evant/kotlin-inject.git")
            url.set("https://github.com/evant/kotlin-inject")
        }
    }
}

publishing {
    if (plugins.hasPlugin("com.android.library")) {
        val android = extensions.getByType<LibraryExtension>()

        // We need to generate source and javadoc artifacts
        val javadoc by tasks.creating(Javadoc::class) {
            //TODO: generate with dokka?
        }

        val javadocJar by tasks.creating(Jar::class) {
            from(javadoc)
            archiveClassifier.set("javadoc")
        }

        val sourcesJar by tasks.creating(Jar::class) {
            from((android.sourceSets.getByName("main").kotlin as AndroidSourceDirectorySet).getSourceFiles())
            archiveClassifier.set("sources")
        }

        // need to run in an afterEvaluate block to wait for variants to be configured
        afterEvaluate {
            publications {
                create<MavenPublication>("lib") {
                    from(components["release"])
                    artifact(javadocJar)
                    artifact(sourcesJar)
                    mavenCentralPom()
                }
            }
        }
    } else {
        // Need to create source, javadoc & publication
        val java = extensions.getByType<JavaPluginExtension>()
        java.withSourcesJar()
        java.withJavadocJar()
        publications {
            create<MavenPublication>("lib") {
                from(components["java"])
                mavenCentralPom()
            }
        }
    }
}

signing {
    setRequired {
        findProperty("signing.keyId") != null
    }

    publishing.publications.all {
        sign(this)
    }
}