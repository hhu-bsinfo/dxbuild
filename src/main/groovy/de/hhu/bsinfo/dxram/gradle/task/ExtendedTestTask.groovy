package de.hhu.bsinfo.dxram.gradle.task

import org.gradle.api.tasks.testing.Test

class ExtendedTestTask extends Test {
    public static final String NAME = "extTest"

    ExtendedTestTask() {
        group = 'verification'
        description = 'Executes all tests located within the extTest source set'

        setTestClassesDirs(project.sourceSets.extTest.output.classesDirs)
        setClasspath(project.sourceSets.extTest.runtimeClasspath)
        outputs.upToDateWhen { false }

        // Run every test in a dedicated JVM to avoid pollution on "distributed" tests over localhost
        forkEvery = 1
        // Limit parallel tests to avoid clashes
        maxParallelForks = 1
    }
}
