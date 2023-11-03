plugins {
    java
    `maven-publish`
}

extensions.configure<PublishingExtension> {
    repositories {
        maven {
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
            name = "paper"
            setUrl("https://repo.constructlegacy.ru/public")
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("Velocity")
                description.set("The modern, next-generation Minecraft server proxy")
                url.set("https://constructlegacy.ru")
                scm {
                    url.set("https://github.com/RedlanceMinecraft/Velocity")
                    connection.set("scm:git:https://github.com/RedlanceMinecraft/Velocity.git")
                    developerConnection.set("scm:git:https://github.com/RedlanceMinecraft/Velocity.git")
                }
            }
        }
    }
}
