// Apply necessary plugins
plugins {
    // Core Kotlin plugin
    kotlin("jvm") version "2.0.20"
    // Allows running the application from Gradle
    application
    // Shadow plugin for creating fat/uber JARs - equivalent to Maven shade plugin
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

// Basic project information - equivalent to Maven's groupId, artifactId, and version
group = "org.example"
version = "1.0-SNAPSHOT"

// Configure all repositories where dependencies can be found
repositories {
    mavenCentral()
    // Custom repositories from your original pom.xml
    maven {
        url = uri("https://s3-eu-west-1.amazonaws.com/maven-repo.schinzel.io/release")
    }
    maven {
        url = uri("https://s3-eu-west-1.amazonaws.com/maven-repo.atexpose.com/release")
    }
    maven {
        url = uri("https://s3-eu-west-1.amazonaws.com/maven-repo.refur.se/release")
    }
}

// Configure the main class for the application
application {
    mainClass.set("io.schinzel.sample.MainKt")
}

// Declare all project dependencies
dependencies {
    // Runtime dependencies
    implementation("commons-io:commons-io:2.11.0")
    implementation("io.javalin:javalin:5.6.3")
    implementation("se.refur:javalin:2.0.0")
    implementation("io.schinzel:basic-utils:1.6.2")
    implementation("io.schinzel:crypto:1.2.6")
    implementation("io.schinzel:basic-utils-kotlin:0.9.3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.20")
    implementation("org.litote.kmongo:kmongo:4.7.1") {
        // Exclude slf4j-nop to prevent multiple bindings
        exclude(group = "org.slf4j", module = "slf4j-nop")
    }
    implementation("org.slf4j:slf4j-simple:2.0.6")

    // Test dependencies
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

// Configure all tasks
tasks {
    // Configure the shadowJar task (creates the fat JAR)
    shadowJar {
        // Set the output directory to project root
        destinationDirectory.set(project.rootDir)
        // Set the JAR filename
        archiveFileName.set("myJar.jar")
        // Merge service files
        mergeServiceFiles()
        // Exclude signing files from dependencies
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }

    // Configure test execution
    test {
        useJUnitPlatform()
    }

    // Configure Kotlin compilation options
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
            apiVersion = "2.0"
            languageVersion = "2.0"
        }
    }

    // Configure Kotlin test compilation options
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    // Configure resource processing - handles HTML and JS files in src/main/kotlin
    processResources {
        from("src/main/kotlin") {
            include("**/*.html")
            include("**/*.js")
        }
    }
}

// Configure Kotlin toolchain
kotlin {
    jvmToolchain(11)
}