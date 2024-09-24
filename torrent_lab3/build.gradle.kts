plugins {
    id("java")
    id("application")
}

group = "nsu_laboratory"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation( "org.apache.logging.log4j:log4j-api:2.19.0")
    implementation ("org.apache.logging.log4j:log4j-core:2.19.0")
    implementation ("com.dampcake:bencode:1.4")
    implementation("com.google.guava:guava:32.0.0-android")
    implementation ("commons-cli:commons-cli:1.4")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "nsu_laboratory.TorrentPeer"
}