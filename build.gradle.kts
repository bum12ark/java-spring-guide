plugins {
    id("org.springframework.boot") version ("2.7.4")
    id("io.spring.dependency-management") version ("1.0.14.RELEASE")
    id("com.diffplug.spotless") version("6.11.0")
    id("jacoco")
    id("java")
}

group = "com.guide"
version = "0.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

springBoot {
    buildInfo()
}

spotless {
    java {
        importOrder(
                "java",
                "javax",
                "lombok",
                "org.springframework",
                "",
                "\\#",
                "org.junit",
                "\\#org.junit",
                "com.guide",
                "\\#com.guide"
        )

        removeUnusedImports()

        googleJavaFormat()

        indentWithTabs(2)
        indentWithSpaces(4)
        trimTrailingWhitespace()
        endWithNewline()
    }
}

configurations {
    compileOnly {
        extendsFrom(annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.34.0")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.4")
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        html.required.set(true)
        xml.required.set(false)
        csv.required.set(false)
    }
    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
    violationRules {
        rule {
            element = "CLASS"
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
            }
            excludes = listOf(
                    "com.guide.Application"
            )
        }
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}