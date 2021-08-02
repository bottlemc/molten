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

    private final Identifier MINECRAFT_CLIENT = Identifier.parse("net/minecraft/client/MinecraftClient");

    @Override
    public byte[] transform(String name, byte[] data) {
        if (name.equals(GAME_RENDERER.getClassName())) {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(data);
            classReader.accept(classNode, 0);

            Identifier render = Identifier.parse("net/minecraft/client/render/GameRenderer#render(FJ)V");

            for(MethodNode methodNode : classNode.methods) {
                if(methodNode.name.equals(render.getMethodName()) && methodNode.desc.equals(render.getMethodDesc())) {
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
        } else if (name.equals(MINECRAFT_CLIENT.getClassName())) {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(data);
            classReader.accept(classNode, 0);

            Identifier handleKeyInput = Identifier.parse("net/minecraft/client/MinecraftClient#handleKeyInput()V");

            for(MethodNode methodNode : classNode.methods) {
                if(methodNode.name.equals(handleKeyInput.getMethodName()) && methodNode.desc.equals(handleKeyInput.getMethodDesc())) {
                    for(AbstractInsnNode node : methodNode.instructions.toArray()) {
                        if(node.getOpcode() == Opcodes.RETURN) {
                            methodNode.instructions.insertBefore(node, new MethodInsnNode(Opcodes.INVOKESTATIC, MoltenHook.class.getName().replace(".", "/"), "onKeyPress", "()V"));
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
