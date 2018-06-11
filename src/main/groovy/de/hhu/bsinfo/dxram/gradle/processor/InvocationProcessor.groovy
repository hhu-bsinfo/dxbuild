package de.hhu.bsinfo.dxram.gradle.processor

import spoon.processing.AbstractProcessor
import spoon.reflect.code.CtInvocation

class InvocationProcessor extends AbstractProcessor<CtInvocation> {

    private final List<String> excludedInvocations;

    InvocationProcessor(List<String> filterList) {

        excludedInvocations = filterList
    }

    @Override
    void process(CtInvocation invocation) {

        if (invocation.getTarget() == null || invocation.getExecutable() == null || invocation.getTarget().getType() == null) {

            return
        }

        String signature = invocation.getTarget().getType().getQualifiedName() + "#" + invocation.getExecutable().toString();

        if (excludedInvocations.any{signature.startsWith(it)}) {

            invocation.delete()
        }
    }
}
