package com.github.glassmc.molten.client.v1_8_9;

import com.github.glassmc.loader.loader.ITransformer;
import com.github.glassmc.loader.util.Identifier;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class MoltenTransformer implements ITransformer {

    private final Identifier GAME_RENDERER = Identifier.parse("net/minecraft/client/render/GameRenderer");
    private final Identifier RENDER = Identifier.parse("net/minecraft/client/render/GameRenderer#render(FJ)V");

    @Override
    public byte[] transform(String name, byte[] data) {
        if(name.equals(GAME_RENDERER.getClassName())) {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(data);
            classReader.accept(classNode, 0);

            for(MethodNode methodNode : classNode.methods) {
                if(methodNode.name.equals(RENDER.getMethodName()) && methodNode.desc.equals(RENDER.getMethodDesc())) {
                    for(AbstractInsnNode node : methodNode.instructions.toArray()) {
                        if(node.getOpcode() == Opcodes.RETURN) {
                            methodNode.instructions.insertBefore(node, new MethodInsnNode(Opcodes.INVOKESTATIC, MoltenHook.class.getName().replace(".", "/"), "onRender", "()V"));
                        }
                    }
                }
            }

            ClassWriter classWriter = new ClassWriter(0);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
        return data;
    }

}
