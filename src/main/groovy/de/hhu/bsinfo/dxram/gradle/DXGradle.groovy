package de.hhu.bsinfo.dxram.gradle

import de.hhu.bsinfo.dxram.gradle.config.BuildType
import de.hhu.bsinfo.dxram.gradle.task.BuildConfigTask
import de.hhu.bsinfo.dxram.gradle.task.DistZipTask
import de.hhu.bsinfo.dxram.gradle.task.ExtractNatives
import de.hhu.bsinfo.dxram.gradle.task.SpoonTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin

class DXGradle implements Plugin<Project> {

    void apply(Project project) {

        project.ext.gitCommit = 'git rev-parse --verify --short HEAD'.execute().text.trim()

        project.ext.currentDate = new Date().format('yyyy-MM-dd HH:mm:ss')

        project.pluginManager.apply(ApplicationPlugin)

        project.configurations {

            nativeImplementation

            implementation.extendsFrom nativeImplementation
        }

        NamedDomainObjectContainer<BuildType> buildTypes = project.container(BuildType)

        project.extensions.add(BuildType.NAME, buildTypes)

        project.sourceSets.main.java.srcDirs = ["${project.projectDir}/src/main/java", "${project.buildDir}/generated"]

        project.afterEvaluate {

            project.tasks.remove(project.tasks.distZip)

            project.tasks.remove(project.tasks.distTar)

            project.tasks.remove(project.tasks.assembleDist)

            project.tasks.create(BuildConfigTask.NAME, BuildConfigTask)

            project.tasks.create(SpoonTask.NAME, SpoonTask)

            project.tasks.create(DistZipTask.NAME, DistZipTask)

            project.tasks.create(ExtractNatives.NAME, ExtractNatives)

            project.tasks.compileJava.dependsOn(SpoonTask.NAME)

            project.tasks.installDist {

                destinationDir = new File("${project.outputDir}/${project.name}")

                preserve {

                    include '*'
                }
            }

            project.tasks.installDist.finalizedBy(ExtractNatives.NAME)

            project.tasks.installDist.doLast {

                project.delete("${project.outputDir}/${project.name}/bin/${project.name}.bat")
            }
        }
    }
}