import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    id("java")
    id("com.diffplug.spotless") version "8.4.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

val redissonVersion by extra("4.3.0")
val lombokVersion by extra("1.18.44")
val junitVersion by extra("5.11.4")
val reactorBomVersion by extra("2025.0.4")
val jacksonVersion by extra("3.1.0")

dependencies {
    implementation("org.redisson:redisson:$redissonVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testImplementation("tools.jackson.core:jackson-core:$jacksonVersion")
    testImplementation("tools.jackson.core:jackson-databind:$jacksonVersion")
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testImplementation(platform("io.projectreactor:reactor-bom:$reactorBomVersion"))
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

spotless {
    encoding("UTF-8")
    java {
        palantirJavaFormat()
        importOrder()
        removeUnusedImports()
        formatAnnotations()
        trimTrailingWhitespace()
        endWithNewline()
        toggleOffOn()
    }

    kotlinGradle {
        ktlint()
    }
}

tasks.test {
    useJUnitPlatform()
}
