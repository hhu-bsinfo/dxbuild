package de.hhu.bsinfo.dxram.gradle.task

import de.hhu.bsinfo.dxram.gradle.extension.DXRamExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskAction

class NativeBuildTask extends DefaultTask {

    public static final String NAME = "nativeBuild"

    NativeBuildTask() {

        group = "build"

        description = "Copies all native libraries to the build directory"

        dependsOn(':dxram-native:build')
    }

    @TaskAction
    def action() {

        def extension = project.extensions.getByType(DXRamExtension)

        System.err.println(extension.outputDir)

        project.copy {

            from(project.findProject(':dxram-native').buildDir) {

                include '**/*.so'
            }

            into "${extension.outputDir}/jni"

            eachFile {

                it.path = name
            }

            includeEmptyDirs = false
        }
    }
}