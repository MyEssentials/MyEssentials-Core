package myessentials.classtransformers;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.world.World;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * Patches BlockTaintFibers to add a hook for the {@link myessentials.event.ModifyBlockEvent}.
 * <br/>
 *
 */
public class BlockTaintFibersTransformer implements IClassTransformer {

    private class BlockTaintFibersGeneratorAdapter extends GeneratorAdapter {

        protected BlockTaintFibersGeneratorAdapter(MethodVisitor mv, int access, String name, String desc) {
            super(Opcodes.ASM4, mv, access, name, desc);
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            super.visitJumpInsn(opcode, label);
            if(opcode == Opcodes.IF_ICMPLT) {
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitVarInsn(Opcodes.ILOAD, 1);
                super.visitVarInsn(Opcodes.ILOAD, 6);
                super.visitInsn(Opcodes.IADD);
                super.visitVarInsn(Opcodes.ILOAD, 3);
                super.visitVarInsn(Opcodes.ILOAD, 7);
                super.visitInsn(Opcodes.IADD);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "myessentials/event/ModifyBiomeEvent", "checkBiome", "(Lnet/minecraft/world/World;II)Z", false);

                Label elseLabel = new Label();
                super.visitJumpInsn(Opcodes.IFEQ, elseLabel);
                super.visitInsn(Opcodes.RETURN);
                super.visitLabel(elseLabel);
            }
        }
    }

    @Override
    public byte[] transform(String name, String srgName, byte[] bytes) {
        if("thaumcraft.common.blocks.BlockTaintFibres".equals(srgName)) {
            ClassReader reader = new ClassReader(bytes);
            ClassWriter writer = new ClassWriter(reader, Opcodes.ASM4);

            ClassVisitor visitor = new ClassVisitor(Opcodes.ASM4, writer) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

                    if("taintBiomeSpread".equals(name)) {
                        return new BlockTaintFibersGeneratorAdapter(methodVisitor, access, name, desc);
                    }

                    return methodVisitor;
                }
            };

            reader.accept(visitor, ClassReader.EXPAND_FRAMES);

            bytes = writer.toByteArray();
        }

        return bytes;
    }
}
