plugins {
    `java-library`
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
}

dependencies {
    api(project(":lab-common"))
    api(libs.org.apache.commons.lang3)
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

javafx {
    version = "21.0.3"
    modules("javafx.controls", "javafx.fxml")
}

application {
    mainClass = "ru.rmntim.client.Client"
    mainModule = "ru.rmntim.client.HelloApplication"
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(
            listOf(
                "compileJava",
                "processResources",
                ":lab-common:jar"
            )
        ) // We need this for Gradle optimization to work
        archiveClassifier.set("fat") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}

description = "lab-client"