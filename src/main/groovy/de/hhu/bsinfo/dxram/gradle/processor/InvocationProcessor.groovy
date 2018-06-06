package de.hhu.bsinfo.dxram.gradle.processor

import spoon.processing.AbstractProcessor
import spoon.reflect.code.CtInvocation

class InvocationProcessor extends AbstractProcessor<CtInvocation> {

    @Override
    void process(CtInvocation invocation) {

        if (invocation.getTarget() == null || invocation.getExecutable() == null || invocation.getTarget().getType() == null) {

            return
        }

        String signature = invocation.getTarget().getType().getQualifiedName() + "#" + invocation.getExecutable().toString();

        if (signature.startsWith("org.apache.logging.log4j.Logger")) {

            System.out.println(signature)
        }
    }
}
