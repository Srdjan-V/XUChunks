package io.github.srdjanv.xuchunks.core;

import io.github.srdjanv.xuchunks.core.transformers.GuiClaimedChunksTransformer;
import io.github.srdjanv.xuchunks.core.transformers.MessageClaimedChunksUpdateTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;

public class XUCTransformer implements IClassTransformer {
    private static final List<IXUCTransformer> transformers;

    static {
        transformers = new ArrayList<>();
        transformers.add(new GuiClaimedChunksTransformer());
        transformers.add(new MessageClaimedChunksUpdateTransformer());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        for (IXUCTransformer transformer : transformers) {
            if (transformer.shouldTransform(name)) {
                return handleTransformation(name, basicClass);
            }
        }
        return basicClass;
    }

    private byte[] handleTransformation(String name, byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader reader = new ClassReader(basicClass);
        reader.accept(classNode, 0);

        boolean computeFrames = false;
        for (IXUCTransformer transformer : transformers) {
            if (transformer.shouldTransform(name)) {
                transformer.transform(classNode);
                computeFrames |= transformer.shouldComputeFrames();
            }
        }

        final ClassWriter writer;
        if (computeFrames) {
            writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        } else writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        classNode.accept(writer);
        return writer.toByteArray();
    }

}
