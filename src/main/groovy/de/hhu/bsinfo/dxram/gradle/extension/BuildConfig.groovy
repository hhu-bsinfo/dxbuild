package de.hhu.bsinfo.dxram.gradle.extension

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeName
import org.gradle.api.InvalidUserDataException

import javax.lang.model.element.Modifier

class BuildConfig {

    public static final String NAME = "buildConfig"

    String className = "BuildConfig"

    String packageName = ""

    Map<String, FieldSpec> fields = new HashMap<>()

    String superBuildConfig

    def <T> void typedField(String name, Class<T> type, T value) {

        if (!type.isInstance(value)) {

            throw new InvalidUserDataException("The provided value doesn't match the specified type")
        }

        TypeName typeName = TypeName.get(type)

        if (typeName.isBoxedPrimitive()) {

            typeName = typeName.unbox()
        }

        FieldSpec fieldSpec = FieldSpec.builder(typeName, name)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(getFormat(value), value)
                .build();

        fields.put(name, fieldSpec);
    }

    void field(String name, String packageName, String className, String initializer) {

        ClassName clazz = ClassName.get(packageName, className)

        FieldSpec fieldSpec = FieldSpec.builder(clazz, name)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$L", initializer)
                .build()

        fields.put(name, fieldSpec)
    }

    void inheritsFrom(String buildConfig) {
        superBuildConfig = buildConfig
    }

    private static <T> String getFormat(T value) {

        return value.class == String.class ? "\$S" : "\$L"
    }
}
