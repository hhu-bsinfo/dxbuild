package de.hhu.bsinfo.dxram.gradle.task

import de.hhu.bsinfo.dxram.gradle.extension.DXRamExtension
import org.gradle.api.tasks.Copy

class CopyConfigTask extends Copy {

    public static final String NAME = "copyConfig"

    CopyConfigTask() {

        def extension = project.extensions.getByType(DXRamExtension)

        group = "dxram"

        description = "Copies the configuration to the build directory"

        from(extension.configDir) {

            it.include("**/*")
        }

        into("${extension.outputDir}/config")
    }
}