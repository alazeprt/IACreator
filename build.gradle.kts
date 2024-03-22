plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.alazeprt.iac"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    version = "17.0.8"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainModule = "com.alazeprt.iac"
    mainClass = "com.alazeprt.iac.IACreator"
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}