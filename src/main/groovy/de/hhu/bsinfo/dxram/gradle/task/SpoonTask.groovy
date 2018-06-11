package de.hhu.bsinfo.dxram.gradle.task

import de.hhu.bsinfo.dxram.gradle.config.BuildType
import de.hhu.bsinfo.dxram.gradle.processor.InvocationProcessor
import org.apache.log4j.Level
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.TaskAction
import spoon.Launcher
import spoon.processing.ProblemFixer
import spoon.processing.Processor
import spoon.reflect.CtModel
import spoon.reflect.declaration.CtElement
import spoon.support.StandardEnvironment

class SpoonTask extends DefaultTask {

    public static final String NAME = 'spoon'

    @Classpath
    private FileCollection classpath

    SpoonTask() {

        group = 'build'

        description = 'Analyzes and transforms all sources using the provided processors'

        dependsOn(BuildConfigTask.NAME)
    }

    @TaskAction
    void action() {

        NamedDomainObjectContainer<BuildType> buildTypes = project.extensions.getByName(BuildType.NAME)

        BuildType buildType = buildTypes.getByName(project.buildType)

        List<String> excludedInvocations = buildType.excludedInvocations

        if (excludedInvocations.isEmpty()) {

            return
        }

        Set<File> sourceFiles = project.sourceSets.main.java.srcDirs

        Set<String> sourceDirs = sourceFiles.collect { it.absolutePath }
                                            .toSet()

        final Launcher launcher = new Launcher()

        sourceDirs.forEach { launcher.addInputResource(it) }

        launcher.getEnvironment().sourceOutputDirectory = new File(project.buildDir, "spoonSources/${buildType.name}")

        launcher.environment.setNoClasspath(true)

        launcher.environment.setAutoImports(true)

        launcher.environment.sourceClasspath = project.configurations.compileClasspath.asPath.split(":")

        launcher.buildModel()

        final CtModel model = launcher.getModel()

        final InvocationProcessor processor = new InvocationProcessor(excludedInvocations)

        model.processWith(processor)

        launcher.prettyprint()

        project.sourceSets.main.java.srcDirs = ["${project.buildDir}/spoonSources/${project.buildType}"]

        project.gradle.buildFinished {

            project.sourceSets.main.java.srcDirs = ["${project.projectDir}/src/main/java", "${project.buildDir}/generated"]
        }
    }
}
