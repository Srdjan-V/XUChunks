package io.github.srdjanv.xuchunks.core.transformers;

import com.feed_the_beast.ftbutilities.gui.GuiClaimedChunks;
import io.github.srdjanv.xuchunks.core.IXUCTransformer;
import io.github.srdjanv.xuchunks.core.TargetClass;
import io.github.srdjanv.xuchunks.core.TransformUtil;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.tree.*;

@TargetClass(GuiClaimedChunks.class)
public class GuiClaimedChunksTransformer implements IXUCTransformer {

    @Override
    public boolean shouldTransform(String clazz) {
        return "com.feed_the_beast.ftbutilities.gui.GuiClaimedChunks".equals(clazz);
    }

    @Override
    public boolean shouldComputeFrames() {
        return true;
    }

    @Override
    public void transform(ClassNode classNode) {
        FieldVisitor fieldVisitor;
        fieldVisitor = classNode.visitField(ACC_PRIVATE | ACC_STATIC, "xUChunks$maxClaimedChunks", "I", null, null);
        fieldVisitor.visitEnd();

        fieldVisitor = classNode.visitField(ACC_PRIVATE | ACC_STATIC, "xUChunks$maxLoadedChunks", "I", null, null);
        fieldVisitor.visitEnd();

        MethodNode onChunkDataUpdate = TransformUtil.findMethod(classNode, "onChunkDataUpdate", "(Lcom/feed_the_beast/ftbutilities/events/chunks/UpdateClientDataEvent;)V");
        int call = TransformUtil.findFirstInstruction(onChunkDataUpdate, insn -> {
            if (insn instanceof FieldInsnNode fieldInsnNode) {
                return (fieldInsnNode.getOpcode() == PUTSTATIC &&
                        fieldInsnNode.owner.equals("com/feed_the_beast/ftbutilities/gui/GuiClaimedChunks") &&
                        fieldInsnNode.name.equals("loadedChunks"));
            }
            return false;
        }, "MessageClaimedChunksUpdate m = event.getMessage();");

        call = TransformUtil.insertLabelInstruction(call, onChunkDataUpdate, insnList -> {
            insnList.add(new VarInsnNode(ALOAD, 1));
            insnList.add(new FieldInsnNode(GETFIELD, "com/feed_the_beast/ftbutilities/net/MessageClaimedChunksUpdate", "xUChunks$maxClaimedChunks", "I"));
            insnList.add(new FieldInsnNode(PUTSTATIC, "com/feed_the_beast/ftbutilities/gui/GuiClaimedChunks", "xUChunks$maxClaimedChunks", "I"));
        });

        TransformUtil.insertLabelInstruction(call, onChunkDataUpdate, insnList -> {
            insnList.add(new VarInsnNode(ALOAD, 1));
            insnList.add(new FieldInsnNode(GETFIELD, "com/feed_the_beast/ftbutilities/net/MessageClaimedChunksUpdate", "xUChunks$maxLoadedChunks", "I"));
            insnList.add(new FieldInsnNode(PUTSTATIC, "com/feed_the_beast/ftbutilities/gui/GuiClaimedChunks", "xUChunks$maxLoadedChunks", "I"));
        });

        //MethodNode addCornerText = findMethod(classNode, "addCornerText", "(Ljava/util/List;Lcom/feed_the_beast/ftblib/lib/gui/misc/GuiChunkSelectorBase$Corner;)V");
    }
}
