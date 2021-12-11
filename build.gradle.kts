import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    application
}

group = "me.ts"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.github.michael-rapp:apriori:2.0.1")
    implementation("com.github.michael-rapp:java-util:2.5.0")
    implementation("com.github.haifengl:smile-kotlin:2.6.0")
    implementation("org.dhatim:fastexcel:0.9.3")
    implementation("org.dhatim:fastexcel-reader:0.9.3")

    implementation ("com.github.zakgof:velvet-video:0.5.2")
    implementation ("com.github.zakgof:velvet-video-natives:0.2.8.full")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}