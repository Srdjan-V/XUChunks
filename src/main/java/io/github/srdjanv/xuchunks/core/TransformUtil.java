package io.github.srdjanv.xuchunks.core;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class TransformUtil {

    public static int insertLabelInstruction(int call, MethodNode node, Consumer<InsnList> instructions) {
        InsnList insns = new InsnList();
        insns.add(new LabelNode(new Label()));
        instructions.accept(insns);

        call = call + insns.size();
        node.instructions.insert(node.instructions.get(call), insns);

        return call;
    }

    public static int findFirstInstruction(MethodNode node, Predicate<AbstractInsnNode> predicate, String instructionName) {
        for (int i = 0; i < node.instructions.size(); ++i) {
            if (predicate.test(node.instructions.get(i))) {
                return i;
            }
        }

        throw new RuntimeException(String.format("Unable to find instruction: %s in Method: %s", instructionName, node.name));
    }

    public static MethodNode findMethod(ClassNode classNode, String name, String desc) {
        for (MethodNode m : classNode.methods) {
            if (m.name.equals(name) && m.desc.equals(desc))
                return m;
        }

        throw new RuntimeException(String.format("Unable to find Method: %s, with desc: %s", name, desc));
    }
}
