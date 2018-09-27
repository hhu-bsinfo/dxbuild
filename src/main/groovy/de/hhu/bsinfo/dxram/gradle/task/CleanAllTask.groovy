package de.hhu.bsinfo.dxram.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CleanAllTask extends DefaultTask {
    public static final String NAME = 'cleanAll'

    CleanAllTask() {
        group = 'build'
        description = 'Clean the current project and all included projects'

        dependsOn(project.gradle.includedBuilds*.task(':clean'))
        dependsOn(project.task(':clean'))
    }

    @TaskAction
    void action() {

    }
}
