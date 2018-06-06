package de.hhu.bsinfo.dxram.gradle.task

import de.hhu.bsinfo.dxram.gradle.processor.InvocationProcessor
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import spoon.Launcher
import spoon.reflect.CtModel

class SpoonTask extends DefaultTask {

    public static final String NAME = 'spoon'

    SpoonTask() {

        group = 'build'

        description = 'Analyzes and transforms all sources using the provided processors'
    }

    @TaskAction
    void action() {

        Set<File> sourceFiles = project.sourceSets.main.java.srcDirs

        Set<String> sourceDirs = sourceFiles.collect { it.absolutePath }
                                            .toSet()

        final Launcher launcher = new Launcher()

        sourceDirs.forEach { launcher.addInputResource(it) }

        launcher.environment.setNoClasspath(true)

        launcher.buildModel()

        final CtModel model = launcher.getModel()

        final InvocationProcessor processor = new InvocationProcessor()

        model.processWith(processor)
    }
}
