package de.hhu.bsinfo.dxram.gradle.task

import de.hhu.bsinfo.dxram.gradle.extension.DXRamExtension
import org.gradle.api.tasks.Copy

class CopyScriptTask extends Copy {

    public static final String NAME = "copyScript"

    CopyScriptTask() {

        def extension = project.extensions.getByType(DXRamExtension)

        group = "dxram"

        description = "Copies the script files to the build directory"

        from(extension.scriptDir) {

            it.include("**/*")
        }

        into("${extension.outputDir}/script")
    }
}