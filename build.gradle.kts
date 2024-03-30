plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "3.0.1"
}

group = "com.alazeprt.iac"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("org.apache.logging.log4j:log4j-api:2.23.1")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
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

jlink {
    launcher {
        name = "IACreator"
    }

    jpackage {
        installerOutputDir = file("$buildDir/installer")
        if(org.gradle.internal.os.OperatingSystem.current().isWindows) {
            installerOptions.add("--win-dir-chooser")
            installerOptions.add("--win-per-user-install")
            installerOptions.add("--win-menu")
            installerOptions.add("--win-shortcut")
        }
    }
}