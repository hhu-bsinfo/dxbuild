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
        if (invocation.getTarget() == null || invocation.getExecutable() == null ||
                invocation.getTarget().getType() == null) {
            return
        }

        // Signature for instance method calls
        String signature = invocation.getTarget().getType().getQualifiedName() + "#" +
                invocation.getExecutable().toString();

        if (excludedInvocations.any { signature.startsWith(it) }) {
            invocation.delete()
        }

        // Signature for static method calls
        signature = "${invocation.getTarget().toString()}#${invocation.getExecutable().toString()}";

        if (excludedInvocations.any { signature.startsWith(it) }) {
            invocation.delete()
        }
    }
}
