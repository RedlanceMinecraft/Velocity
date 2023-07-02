/*
 * Copyright (C) 2023 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.velocitypowered.script

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType

class VelocityPublishPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.afterEvaluate {
        configure()
    }
    private fun Project.configure() {
        apply<JavaBasePlugin>()
        apply<MavenPublishPlugin>()
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
                            url.set("https://github.com/TurboMC/Velocity")
                            connection.set("scm:git:https://github.com/TurboMC/Velocity.git")
                            developerConnection.set("scm:git:https://github.com/TurboMC/Velocity.git")
                        }
                    }
                }
            }
        }
    }
}
