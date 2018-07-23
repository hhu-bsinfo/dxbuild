package de.hhu.bsinfo.dxram.gradle.task

import de.hhu.bsinfo.dxram.gradle.extension.DXRamExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskAction

class ExtractNatives extends DefaultTask {

    public static final String NAME = "extractNatives"

    private static final String SHARED_LIBRARY_EXTENSION = ".so"

    private static final String NATIVE_LIBRARY = "dxutils"

    ExtractNatives() {

        group = "build"

        description = "Extracts all native binaries to the distribution directory"
    }

    @TaskAction
    void action() {

        def nativeBinaries = new ArrayList<File>()

        if (project.configurations.nativeImplementation.files) {

            nativeBinaries.addAll(project.zipTree(project.configurations.nativeImplementation.files
                    .find{it.name.contains(NATIVE_LIBRARY)})
                    .findAll{it.name.endsWith(SHARED_LIBRARY_EXTENSION)})
        }

        if (project.configurations.nativeApi.files) {

            nativeBinaries.addAll(project.zipTree(project.configurations.nativeApi.files
                    .find{it.name.contains(NATIVE_LIBRARY)})
                    .findAll{it.name.endsWith(SHARED_LIBRARY_EXTENSION)})
        }

        if (nativeBinaries.isEmpty()) {

            return
        }

        project.copy {

            it.from(*nativeBinaries)

            into "${project.outputDir}/${project.name}/jni"

            eachFile {
                it.path = name
            }

            includeEmptyDirs = false
        }
    }
}