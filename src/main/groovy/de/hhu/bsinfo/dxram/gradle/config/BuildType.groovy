package de.hhu.bsinfo.dxram.gradle.config

import de.hhu.bsinfo.dxram.gradle.extension.BuildConfig
import org.gradle.util.ConfigureUtil

class BuildType {

    private static final String LEVEL_ALL = "ALL";

    private static final String LEVEL_RELEASE = "RELEASE";

    private static final String LEVEL_PERFORMANCE = "PERFORMANCE";

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

    @SuppressWarnings("GroovyFallthrough")
    void excludeLogger(String level) {
        switch (level) {
            case LEVEL_ALL:
                excludeInvocations 'org.apache.logging.log4j.Logger#error'
                excludeInvocations 'org.apache.logging.log4j.Logger#warn'
            case LEVEL_PERFORMANCE:
                excludeInvocations 'org.apache.logging.log4j.Logger#info'
            case LEVEL_RELEASE:
                excludeInvocations 'org.apache.logging.log4j.Logger#debug'
                excludeInvocations 'org.apache.logging.log4j.Logger#trace'
        }
    }

    void excludeStatistics() {

        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.Throughput#startDebug'
        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.Throughput#addDebug'
        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.Throughput#stopDebug'

        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.ThroughputPool#startDebug'
        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.ThroughputPool#addDebug'
        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.ThroughputPool#stopDebug'

        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.Time#startDebug'
        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.Time#stopDebug'

        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.Timeline#startDebug'
        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.Timeline#nextSectionDebug'
        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.Timeline#stopDebug'

        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.TimePercentile#recordDebug'

        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.TimePercentilePool#startDebug'

        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.TimePool#startDebug'
        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.TimePool#stopDebug'

        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.Value#incDebug'
        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.Value#addDebug'

        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.ValuePercentile#recordDebug'

        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.ValuePool#incDebug'
        excludeInvocations 'de.hhu.bsinfo.dxutils.stats.ValuePool#addDebug'
    }
}
