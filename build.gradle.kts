import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.tasks.testing.Test

plugins {
    java
    id("com.diffplug.spotless") version "8.7.0"
}

repositories {
    mavenCentral()
}

allprojects {
    group = "com.sanwenyukaochi.redisson"
    version = "0.0.1-SNAPSHOT"
    pluginManager.apply("com.diffplug.spotless")

    pluginManager.withPlugin("com.diffplug.spotless") {
        extensions.configure<SpotlessExtension> {
            encoding("UTF-8")
            java {
                target("**/*.java")
                forbidWildcardImports()
                forbidModuleImports()
                googleJavaFormat()
                    .aosp()
                    .reflowLongStrings(false)
                    .formatJavadoc(true)
                    .reorderImports(true)
                importOrder()
                removeUnusedImports()
                formatAnnotations()
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }

            kotlin {
                target("**/*.kt")
                ktlint()
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }

            kotlinGradle {
                target("**/*.gradle.kts")
                ktlint()
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }

            gherkin {
                target("**/*.feature")
                gherkinUtils()
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }

            toml {
                target("**/*.toml")
                versionCatalog()
                    .stripQuotedKeys(true)
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }

            json {
                target("**/*.json")
                gson()
                    .indentWithSpaces(4)
                    .sortByKeys()
                    .escapeHtml()
                trimTrailingWhitespace()
                endWithNewline()
                toggleOffOn()
            }
        }
    }

    pluginManager.withPlugin("java") {
        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_26
            targetCompatibility = JavaVersion.VERSION_26
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(26))
            }
            withSourcesJar()
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
    dependencies {
        implementation("org.redisson:redisson:${libs.versions.redisson.get()}")
        implementation("tools.jackson.core:jackson-databind:3.0.0")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher:${libs.versions.junit.get()}")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:${libs.versions.junit.get()}")
        testImplementation("org.junit.jupiter:junit-jupiter-api:${libs.versions.junit.get()}")
        testImplementation("org.junit.jupiter:junit-jupiter:${libs.versions.junit.get()}")
    }
}

subprojects {
}
