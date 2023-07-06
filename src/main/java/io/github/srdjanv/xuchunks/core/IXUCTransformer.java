package io.github.srdjanv.xuchunks.core;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public interface IXUCTransformer extends Opcodes {
    boolean shouldTransform(String clazz);
    boolean shouldComputeFrames();
    void transform(ClassNode classNode);
}
