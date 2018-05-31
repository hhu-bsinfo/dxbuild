package de.hhu.bsinfo.dxram.gradle

import de.hhu.bsinfo.dxram.gradle.extension.BuildConfigExtension
import de.hhu.bsinfo.dxram.gradle.extension.DXRamExtension
import de.hhu.bsinfo.dxram.gradle.task.BuildConfigTask
import de.hhu.bsinfo.dxram.gradle.task.NativeBuildTask
import de.hhu.bsinfo.dxram.gradle.task.DistributionTask
import de.hhu.bsinfo.dxram.gradle.task.FatJarTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.compile.JavaCompile

class DXRamPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.pluginManager.apply(JavaPlugin)

        project.extensions.create(DXRamExtension.NAME, DXRamExtension)

        project.extensions.create(BuildConfigExtension.NAME, BuildConfigExtension)

        project.sourceSets.main.java.srcDirs += "${project.buildDir}/generated"

        project.afterEvaluate {

            project.tasks.create(DistributionTask.NAME, DistributionTask)

            project.tasks.create(NativeBuildTask.NAME, NativeBuildTask)

            project.tasks.create(FatJarTask.NAME, FatJarTask)

            project.tasks.create(BuildConfigTask.NAME, BuildConfigTask)

            project.tasks.compileJava.dependsOn(BuildConfigTask.NAME)
        }
    }
}