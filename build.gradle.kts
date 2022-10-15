plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.7.0"
}

group "com.hemajoo.commerce.plugin"
version "1.3.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://gitlab.com/api/v4/projects/18964146/packages/maven")
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2021.3")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("properties"))
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("org.ressec.core:core-extension-i18n:1.0")
    //implementation 'org.ressec.lychee:lychee-localization:0.4.0-SNAPSHOT' // Future dependency to use
    testCompileOnly("junit:junit:4.13.2")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("213")
        untilBuild.set("223.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
