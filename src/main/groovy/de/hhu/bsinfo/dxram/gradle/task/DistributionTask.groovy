package de.hhu.bsinfo.dxram.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class DistributionTask extends DefaultTask {

    public static final String NAME = "installDist"

    DistributionTask() {

        group = "distribution"

        description = "Creates a ready to use distribution"

        dependsOn(FatJarTask.NAME, NativeBuildTask.NAME)
    }

    @TaskAction
    void action() {

        project.copy {

            from("${project.rootDir}/dxram/src/main/dist") {

                it.include("**/*")
            }

            into("${project.outputDir}/dxram")
        }
    }
}