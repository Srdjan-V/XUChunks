package io.github.srdjanv.xuchunks.core.transformers;

import com.feed_the_beast.ftbutilities.net.MessageClaimedChunksUpdate;
import io.github.srdjanv.xuchunks.core.IXUCTransformer;
import io.github.srdjanv.xuchunks.core.TargetClass;
import io.github.srdjanv.xuchunks.core.TransformUtil;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

@TargetClass(MessageClaimedChunksUpdate.class)
public class MessageClaimedChunksUpdateTransformer implements IXUCTransformer {
    @Override
    public boolean shouldTransform(String clazz) {
        return "com.feed_the_beast.ftbutilities.net.MessageClaimedChunksUpdate".equals(clazz);
    }

    @Override
    public boolean shouldComputeFrames() {
        return true;
    }

    @Override
    public void transform(ClassNode classNode) {
        FieldVisitor fieldVisitor;
        int call;

        fieldVisitor = classNode.visitField(ACC_PUBLIC, "xUChunks$maxClaimedChunks", "I", null, null);
        fieldVisitor.visitEnd();

        fieldVisitor = classNode.visitField(ACC_PUBLIC, "xUChunks$maxLoadedChunks", "I", null, null);
        fieldVisitor.visitEnd();

        {
            var constructor = TransformUtil.findMethod(classNode, "<init>", "(IILnet/minecraft/entity/player/EntityPlayer;)V");
            call = TransformUtil.findFirstInstruction(constructor, insnNode -> {
                if (insnNode instanceof MethodInsnNode methodInsnNode) {
                    return methodInsnNode.owner.equals("com/feed_the_beast/ftbutilities/data/FTBUtilitiesTeamData") &&
                            methodInsnNode.name.equals("get") &&
                            methodInsnNode.desc.equals("(Lcom/feed_the_beast/ftblib/lib/data/ForgeTeam;)Lcom/feed_the_beast/ftbutilities/data/FTBUtilitiesTeamData;");
                }
                return false;
            }, "FTBUtilitiesTeamData teamData = FTBUtilitiesTeamData.get(p.team);");
            call++;
            call = TransformUtil.insertLabelInstruction(call, constructor, insnList -> {
                insnList.add(new VarInsnNode(ALOAD, 0));
                insnList.add(new VarInsnNode(ALOAD, 5));
                insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/feed_the_beast/ftbutilities/data/FTBUtilitiesTeamData", "xUChunks$getMaxClaimChunks", "()I", false));
                insnList.add(new FieldInsnNode(PUTFIELD, "com/feed_the_beast/ftbutilities/net/MessageClaimedChunksUpdate", "xUChunks$maxClaimedChunks", "I"));
            });

            TransformUtil.insertLabelInstruction(call, constructor, insnList -> {
                insnList.add(new VarInsnNode(ALOAD, 0));
                insnList.add(new VarInsnNode(ALOAD, 5));
                insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/feed_the_beast/ftbutilities/data/FTBUtilitiesTeamData", "xUChunks$getMaxChunkloaderChunksGrid", "()I", false));
                insnList.add(new FieldInsnNode(PUTFIELD, "com/feed_the_beast/ftbutilities/net/MessageClaimedChunksUpdate", "xUChunks$maxLoadedChunks", "I"));
            });
        }

        {
            var writeData = TransformUtil.findMethod(classNode, "writeData", "(Lcom/feed_the_beast/ftblib/lib/io/DataOut;)V");
            call = TransformUtil.insertLabelInstruction(0, writeData, insnList -> {
                insnList.add(new VarInsnNode(ALOAD, 1));
                insnList.add(new VarInsnNode(ALOAD, 0));
                insnList.add(new FieldInsnNode(GETFIELD, "com/feed_the_beast/ftbutilities/net/MessageClaimedChunksUpdate", "xUChunks$maxClaimedChunks", "I"));
                insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/feed_the_beast/ftblib/lib/io/DataOut", "writeVarInt", "(I)V", false));
            });
            TransformUtil.insertLabelInstruction(call, writeData, insnList -> {
                insnList.add(new VarInsnNode(ALOAD, 1));
                insnList.add(new VarInsnNode(ALOAD, 0));
                insnList.add(new FieldInsnNode(GETFIELD, "com/feed_the_beast/ftbutilities/net/MessageClaimedChunksUpdate", "xUChunks$maxLoadedChunks", "I"));
                insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/feed_the_beast/ftblib/lib/io/DataOut", "writeVarInt", "(I)V", false));
            });
        }

        {
            var readData = TransformUtil.findMethod(classNode, "readData", "(Lcom/feed_the_beast/ftblib/lib/io/DataIn;)V");
            call = TransformUtil.insertLabelInstruction(0, readData, insnList -> {
                insnList.add(new VarInsnNode(ALOAD, 0));
                insnList.add(new VarInsnNode(ALOAD, 1));
                insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/feed_the_beast/ftblib/lib/io/DataIn", "readVarInt", "()I", false));
                insnList.add(new FieldInsnNode(PUTFIELD, "com/feed_the_beast/ftbutilities/net/MessageClaimedChunksUpdate", "xUChunks$maxClaimedChunks", "I"));
            });
            TransformUtil.insertLabelInstruction(call, readData, insnList -> {
                insnList.add(new VarInsnNode(ALOAD, 0));
                insnList.add(new VarInsnNode(ALOAD, 1));
                insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/feed_the_beast/ftblib/lib/io/DataIn", "readVarInt", "()I", false));
                insnList.add(new FieldInsnNode(PUTFIELD, "com/feed_the_beast/ftbutilities/net/MessageClaimedChunksUpdate", "xUChunks$maxLoadedChunks", "I"));
            });
        }
    }
}
