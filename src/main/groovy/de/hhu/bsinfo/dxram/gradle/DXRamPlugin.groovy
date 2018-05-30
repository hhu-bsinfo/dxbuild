package de.hhu.bsinfo.dxram.gradle

import de.hhu.bsinfo.dxram.gradle.extension.DXRamExtension
import de.hhu.bsinfo.dxram.gradle.task.NativeBuildTask
import de.hhu.bsinfo.dxram.gradle.task.DistributionTask
import de.hhu.bsinfo.dxram.gradle.task.FatJarTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class DXRamPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.pluginManager.apply(JavaPlugin)

        project.extensions.create(DXRamExtension.NAME, DXRamExtension)

        project.afterEvaluate {

            project.tasks.create(DistributionTask.NAME, DistributionTask)

            project.tasks.create(NativeBuildTask.NAME, NativeBuildTask)

            project.tasks.create(FatJarTask.NAME, FatJarTask)
        }
    }
}