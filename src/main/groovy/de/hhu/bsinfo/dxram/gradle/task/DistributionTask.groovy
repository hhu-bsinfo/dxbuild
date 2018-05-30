package de.hhu.bsinfo.dxram.gradle.task

import de.hhu.bsinfo.dxram.gradle.extension.DXRamExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

class DistributionTask extends DefaultTask {

    @Option(option = "target", description = "The target directory")
    String targetDir

    public static final String NAME = "installDist"

    DistributionTask() {

        group = "distribution"

        description = "Creates a ready to use distribution"

        finalizedBy(FatJarTask.NAME, NativeBuildTask.NAME)
    }

    @TaskAction
    def action() {

        def extension = project.extensions.getByType(DXRamExtension)

        extension.outputDir = targetDir == null ? extension.outputDir : targetDir

        System.err.println(extension.outputDir)

        project.copy {

            from("${project.rootDir}/core/src/main/dist") {

                it.include("**/*")
            }

            into("${extension.outputDir}")
        }
    }
}