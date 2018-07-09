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

        dependsOn(':native:build')
    }

    @TaskAction
    void action() {

        project.copy {

            from(project.findProject(':native').buildDir) {

                include '**/*.so'
            }

            into "${project.outputDir}/${project.name}/jni"

            eachFile {

                it.path = name
            }

            includeEmptyDirs = false
        }
    }
}