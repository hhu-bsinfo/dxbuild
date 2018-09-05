package de.hhu.bsinfo.dxram.gradle.task


import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.tasks.testing.Test

class ExtendedTestTask extends Test {

    public static final String NAME = "extTest"

    ExtendedTestTask() {
        group = 'verification'
        description = 'Executes all tests located within the extTest source set'


        setTestClassesDirs(project.sourceSets.extTest.output.classesDirs)
        setClasspath(project.sourceSets.extTest.runtimeClasspath)
        outputs.upToDateWhen { false }
    }
}
