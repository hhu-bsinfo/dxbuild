package de.hhu.bsinfo.dxram.gradle.task

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import de.hhu.bsinfo.dxram.gradle.config.BuildType
import de.hhu.bsinfo.dxram.gradle.extension.BuildConfig
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
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
    void action() {

        NamedDomainObjectContainer<BuildType> buildTypes = project.extensions.getByName(BuildType.NAME)

        BuildType buildType = buildTypes.getByName(project.buildType)

        BuildConfig buildConfig = buildType.buildConfig

        if (buildConfig.superBuildConfig != null) {

            BuildConfig superBuildConfig = buildTypes.getByName(buildConfig.superBuildConfig).buildConfig

            superBuildConfig.fields.putAll(buildConfig.fields)

            buildConfig.fields = superBuildConfig.fields
        }

        AnnotationSpec annotation = AnnotationSpec.builder(Generated)
            .addMember("value", "\$S", "de.hhu.bsinfo.dxgradle")
            .build()

        TypeSpec classSpec = TypeSpec.classBuilder(buildConfig.className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addFields(buildConfig.fields.values())
            .addAnnotation(annotation)
            .build()

        String packageName = buildConfig.packageName

        packageName = packageName.isEmpty() ? "${project.group}.${project.name}.generated" : packageName;

        JavaFile javaFile = JavaFile.builder(packageName, classSpec)
                .build()

        javaFile.writeTo(new File(project.buildDir, "generated"))
    }
}
