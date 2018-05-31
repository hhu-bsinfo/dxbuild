package de.hhu.bsinfo.dxram.gradle.task

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import de.hhu.bsinfo.dxram.gradle.extension.BuildConfigExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import javax.annotation.Generated
import javax.lang.model.element.Modifier

class BuildConfigTask extends DefaultTask {

    public static final String NAME = "generateBuildConfig"

    BuildConfigTask() {

        group = 'build'

        description = 'Generates the build configuration class'
    }

    @TaskAction
    def action() {

        def extension = project.extensions.getByType(BuildConfigExtension)

        def annotation = AnnotationSpec.builder(Generated)
            .addMember("value", "\$S", "de.hhu.bsinfo.dxgradle")
            .build()

        def classSpec = TypeSpec.classBuilder(extension.className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addFields(extension.fields)
            .addAnnotation(annotation)
            .build()

        def packageName = extension.packageName

        packageName = packageName.isEmpty() ? project.group + ".generated" : packageName;

        def javaFile = JavaFile.builder(packageName, classSpec)
                .build()

        javaFile.writeTo(new File(project.buildDir, "generated"))
    }
}
