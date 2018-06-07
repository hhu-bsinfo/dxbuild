package de.hhu.bsinfo.dxram.gradle

import de.hhu.bsinfo.dxram.gradle.config.BuildType
import de.hhu.bsinfo.dxram.gradle.extension.DXRamExtension
import de.hhu.bsinfo.dxram.gradle.task.BuildConfigTask
import de.hhu.bsinfo.dxram.gradle.task.DistZipTask
import de.hhu.bsinfo.dxram.gradle.task.DistributionTask
import de.hhu.bsinfo.dxram.gradle.task.FatJarTask
import de.hhu.bsinfo.dxram.gradle.task.NativeBuildTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class DXRamPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.ext.gitCommit = 'git rev-parse --verify --short HEAD'.execute().text.trim()

        project.pluginManager.apply(JavaPlugin)

        NamedDomainObjectContainer<BuildType> buildTypes = project.container(BuildType)

        project.extensions.add(BuildType.NAME, buildTypes)

        project.extensions.create(DXRamExtension.NAME, DXRamExtension)

        project.sourceSets.main.java.srcDirs += "${project.buildDir}/generated"

        project.afterEvaluate {

            project.tasks.create(DistributionTask.NAME, DistributionTask)

            project.tasks.create(DistZipTask.NAME, DistZipTask)

            project.tasks.create(NativeBuildTask.NAME, NativeBuildTask)

            project.tasks.create(FatJarTask.NAME, FatJarTask)

            project.tasks.create(BuildConfigTask.NAME, BuildConfigTask)

            project.tasks.compileJava.dependsOn(BuildConfigTask.NAME)
        }
    }
}