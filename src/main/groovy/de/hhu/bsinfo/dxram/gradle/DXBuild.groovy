package de.hhu.bsinfo.dxram.gradle

import de.hhu.bsinfo.dxram.gradle.config.BuildVariant
import de.hhu.bsinfo.dxram.gradle.processor.InvocationProcessor
import de.hhu.bsinfo.dxram.gradle.task.*
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaLibraryPlugin
import spoon.Launcher
import spoon.reflect.CtModel

class DXBuild implements Plugin<Project> {
    void apply(Project project) {
        project.ext.gitCommit = 'git rev-parse --verify --short HEAD'.execute().text.trim()
        project.ext.currentDate = new Date().format('yyyy-MM-dd HH:mm:ss')

        project.pluginManager.apply(ApplicationPlugin)
        project.pluginManager.apply(JavaLibraryPlugin)

        project.configurations {
            nativeImplementation
            implementation.extendsFrom nativeImplementation
            nativeApi
            api.extendsFrom nativeApi
        }

        NamedDomainObjectContainer<BuildVariant> buildVariants = project.container(BuildVariant)
        project.extensions.add(BuildVariant.NAME, buildVariants)

        project.sourceSets.main.java.srcDirs = ["${project.projectDir}/src/main/java", "${project.buildDir}/generated"]

        project.sourceSets {
            extTest {
                java {
                    compileClasspath += main.output + test.output
                    runtimeClasspath += main.output + test.output
                    srcDir project.file("src/extTest/java")
                }
                resources.srcDir project.file("src/extTest/resources")
            }
        }

        project.configurations {
            extTestImplementation.extendsFrom testImplementation
        }

        project.tasks.create(ExtendedTestTask.NAME, ExtendedTestTask)

        project.afterEvaluate {
            project.tasks.compileJava.doFirst { runPreProcessor(project) }

            project.tasks.remove(project.tasks.distZip)
            project.tasks.remove(project.tasks.distTar)
            project.tasks.remove(project.tasks.assembleDist)

            project.tasks.create(BuildConfigTask.NAME, BuildConfigTask)
            project.tasks.create(DistZipTask.NAME, DistZipTask)
            project.tasks.create(ExtractNatives.NAME, ExtractNatives)
            project.tasks.create(CleanAllTask.NAME, CleanAllTask)

            project.tasks.compileJava.dependsOn(BuildConfigTask.NAME)

            project.tasks.installDist {
                destinationDir = new File("${project.outputDir}/${project.name}")

                preserve {
                    include '*'
                }
            }

            project.tasks.installDist.finalizedBy(ExtractNatives.NAME)

            project.tasks.installDist.doLast {
                project.delete("${project.outputDir}/${project.name}/bin/${project.name}.bat")
            }
        }
    }

    static void runPreProcessor(Project project) {
        NamedDomainObjectContainer<BuildVariant> buildVariants = project.extensions.getByName(BuildVariant.NAME)

        if (!project.hasProperty("buildVariant")) {
            return
        }

        BuildVariant buildVariant = buildVariants.getByName(project.buildVariant)
        List<String> excludedInvocations = buildVariant.excludedInvocations

        if (excludedInvocations.isEmpty()) {
            return
        }

        Set<File> sourceFiles = project.sourceSets.main.java.srcDirs
        Set<String> sourceDirs = sourceFiles.collect { it.absolutePath }.toSet()

        final Launcher launcher = new Launcher()

        sourceDirs.forEach { launcher.addInputResource(it) }

        launcher.getEnvironment().sourceOutputDirectory =
                new File(project.buildDir, "spoonSources/${buildVariant.name}")

        launcher.environment.setNoClasspath(true)
        launcher.environment.setAutoImports(false)
        launcher.environment.level = "OFF"
        launcher.environment.sourceClasspath = project.configurations.compileClasspath.asPath.split(":")

        launcher.buildModel()

        final CtModel model = launcher.getModel()
        final InvocationProcessor processor = new InvocationProcessor(excludedInvocations)

        model.processWith(processor)

        launcher.prettyprint()

        project.sourceSets.main.java.srcDirs = ["${project.buildDir}/spoonSources/${project.buildVariant}"]

        project.gradle.buildFinished {
            project.sourceSets.main.java.srcDirs =
                    ["${project.projectDir}/src/main/java", "${project.buildDir}/generated"]
        }
    }
}
