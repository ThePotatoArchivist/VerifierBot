plugins {
    kotlin("jvm") version "2.3.10"
}

group = "archives.tater.bot.verifier"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("dev.kord:kord-core:${properties["kord_version"]}")
    implementation("io.github.cdimascio:dotenv-kotlin:${properties["dotenv_version"]}")
    implementation("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}