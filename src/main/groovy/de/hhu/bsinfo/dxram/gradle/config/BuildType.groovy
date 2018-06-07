package de.hhu.bsinfo.dxram.gradle.config

import de.hhu.bsinfo.dxram.gradle.extension.BuildConfig
import org.gradle.util.ConfigureUtil

class BuildType {

    public static final String NAME = "buildTypes"

    final name

    BuildConfig buildConfig

    List<String> excludedInvocations

    BuildType(String name) {
        this.name = name
        this.buildConfig = new BuildConfig()
        this.excludedInvocations = new ArrayList<>()
    }

    void buildConfig(Closure closure) {
        ConfigureUtil.configure(closure, buildConfig)
    }

    void excludeInvocations(String... invocations) {
        excludedInvocations.addAll(invocations)
    }
}
