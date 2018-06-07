package de.hhu.bsinfo.dxram.gradle.task

import de.hhu.bsinfo.dxram.gradle.extension.DXRamExtension
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.Jar
import java.util.*

class FatJarTask extends Jar {

    public static final String NAME = "fatJar"

    FatJarTask() {

        group = 'jar'

        description = 'Creates a JAR archive containing all dependencies'

        manifest {

            it.attributes (
                    'BuildUser': System.properties['user.name'],
                    'BuildDate': new Date().format('yyyy-MM-dd HH:mm:ss'),
                    'Main-Class': 'de.hhu.bsinfo.dxram.DXRAMMain',
                    'Class-Path': project.configurations.compileClasspath.collect{ it.getName() }.join(' ')
            )
        }

        baseName = 'dxram'

        version = ''

        from { project.configurations.compileClasspath.collect { it.isDirectory() ? it : project.zipTree(it) } }

        with project.tasks.getByName("jar") as CopySpec
    }

    @TaskAction
    void action() {

        project.copy {

            from("${project.buildDir}/libs") {

                it.include("dxram.jar")
            }

            into("${project.outputDir}/dxram")
        }
    }
}