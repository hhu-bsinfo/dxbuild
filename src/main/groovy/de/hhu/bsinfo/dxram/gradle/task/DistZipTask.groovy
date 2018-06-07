package de.hhu.bsinfo.dxram.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.Zip

class DistZipTask extends Zip {

    public static final String NAME = "distZip"

    DistZipTask() {

        group = 'distribution'

        description = 'Creates a zip file containing a ready to use distribution'

        dependsOn(DistributionTask.NAME)

        from "${project.outputDir}/dxram"

        include '**/*'

        archiveName = 'dxram.zip'

        destinationDir project.outputDir.startsWith('/') ? new File(project.outputDir) : new File(project.projectDir, project.outputDir)
    }
}
