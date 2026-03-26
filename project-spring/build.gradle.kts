plugins {
    id("java")
    id("org.springframework.boot") version "4.0.4"
    id("io.spring.dependency-management") version "1.1.7"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

val redissonVersion by extra("4.3.0")
val lombokVersion by extra("1.18.44")
val junitVersion by extra("6.1.0-M1")
val reactorBom by extra("2025.0.4")


dependencies {
    implementation ("org.springframework.boot:spring-boot-starter")
    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    implementation ("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-aop:3.5.7")
    implementation ("org.springframework.boot:spring-boot-starter-data-redis")

    implementation ("org.redisson:redisson:$redissonVersion")

    compileOnly ("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor ("org.projectlombok:lombok:$lombokVersion")

    testCompileOnly ("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor ("org.projectlombok:lombok:$lombokVersion")

    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")
    testImplementation ("io.projectreactor:reactor-test")
    
//    testImplementation platform("io.projectreactor:reactor-bom:${reactorBom}")
//    testImplementation "io.projectreactor:reactor-test"
//
//    testImplementation "org.junit.jupiter:junit-jupiter-engine:${junitVersion}"
//    testRuntimeOnly "org.junit.platform:junit-platform-launcher:${junitVersion}"
//    testImplementation "org.junit.jupiter:junit-jupiter-api:${junitVersion}"
//    testImplementation "org.junit.jupiter:junit-jupiter:${junitVersion}"
}

tasks.test {
    useJUnitPlatform()
}