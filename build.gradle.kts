plugins {
    `java-library`
    `maven-publish`
}

val jitpackGroup = System.getenv("GROUP")
val jitpackRepository = System.getenv("ARTIFACT")
val jitpackVersion = System.getenv("VERSION")
val standaloneBuild = project == rootProject

group = when {
    jitpackGroup == null -> "com.shyamstudio.coinflip"
    standaloneBuild -> jitpackGroup
    else -> "$jitpackGroup.$jitpackRepository"
}
version = jitpackVersion ?: "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("api") {
            from(components["java"])
            artifactId = if (standaloneBuild && jitpackRepository != null) {
                jitpackRepository
            } else {
                "coinflip-api"
            }
            pom {
                name.set("Shyam CoinFlip API")
                description.set("Public integration contracts for Shyam CoinFlip")
                url.set("https://github.com/Ravi-DevX/" +
                        (if (standaloneBuild && jitpackRepository != null) jitpackRepository else "Shyam-CoinFlip"))
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("ShyamStudio")
                        name.set("ShyamStudio")
                    }
                }
            }
        }
    }
}
