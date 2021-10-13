plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    application
}

group = "com.cinema"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
    maven( "https://jitpack.io")
}

val koinVersion = "3.1.2"
val ktorVersion = "1.6.4"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("com.github.ben-manes.caffeine:caffeine:3.0.4")

    implementation("software.amazon.awssdk:dynamodb:2.17.57")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.1")

    implementation("com.github.papsign:Ktor-OpenAPI-Generator:-SNAPSHOT")



    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("org.mockito:mockito-core:3.12.4")
    testImplementation("org.mockito:mockito-junit-jupiter:3.12.4")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("org.mock-server:mockserver-junit-jupiter:5.11.1")
    testImplementation(platform("org.testcontainers:testcontainers-bom:1.16.0"))
    testImplementation("org.testcontainers:junit-jupiter:1.16.0")
    testImplementation("ch.qos.logback:logback-classic:1.2.5")
    testImplementation("ch.qos.logback:logback-core:1.2.5")


}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}