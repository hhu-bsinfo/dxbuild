package de.hhu.bsinfo.dxram.gradle.task

import de.hhu.bsinfo.dxram.gradle.config.BuildVariant
import de.hhu.bsinfo.dxram.gradle.processor.InvocationProcessor
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.TaskAction
import spoon.Launcher
import spoon.reflect.CtModel

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

        Set<String> sourceDirs = sourceFiles.collect { it.absolutePath }
                                            .toSet()

        final Launcher launcher = new Launcher()

        sourceDirs.forEach { launcher.addInputResource(it) }

        launcher.getEnvironment().sourceOutputDirectory = new File(project.buildDir, "spoonSources/${buildVariant.name}")

        launcher.environment.setNoClasspath(true)

        launcher.environment.setAutoImports(false)

        launcher.environment.sourceClasspath = project.configurations.compileClasspath.asPath.split(":")

        launcher.buildModel()

        final CtModel model = launcher.getModel()

        final InvocationProcessor processor = new InvocationProcessor(excludedInvocations)

        model.processWith(processor)

        launcher.prettyprint()

        project.sourceSets.main.java.srcDirs = ["${project.buildDir}/spoonSources/${project.buildVariant}"]

        project.gradle.buildFinished {

            project.sourceSets.main.java.srcDirs = ["${project.projectDir}/src/main/java", "${project.buildDir}/generated"]
        }
    }
}
