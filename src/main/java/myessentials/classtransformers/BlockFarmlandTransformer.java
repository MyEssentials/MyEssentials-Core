package myessentials.classtransformers;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * Patches BlockFarmland to add a hook for the {@link myessentials.event.BlockTrampleEvent}.
 * <br/>
 * The final code would be:
 * <pre><code>
 *     public class BlockFarmland extends Block{
 *         // ... original fields and methods
 *
 *         public void onFallenUpon(World world, int x, int y, int z, Entity entity, float p6){
 *             // ... original code
 *             if(BlockTrampleEvent.fireEvent(entity, this, x, y, z))
 *               return;
 *             // ... original code
 *         }
 *
 *         // ... original methods
 *     }
 * </code></pre>
 */
public class BlockFarmlandTransformer implements IClassTransformer {

    /**
     * Generates code before the first ALOAD 1 that comes after the first RETURN.<br>
     * The code that is generated is:
     * <pre><code>if(BlockTrampleEvent.fireEvent(entity, this, x, y, z)) return;</code></pre>
     */
    private class FarmlandGeneratorAdapter extends GeneratorAdapter {
        boolean patched = false;
        boolean waitingALoad1 = false;

        protected FarmlandGeneratorAdapter(MethodVisitor mv, int access, String name, String desc) {
            super(Opcodes.ASM4, mv, access, name, desc);
        }

        @Override
        public void visitInsn(int opcode) {
            super.visitInsn(opcode);
            if(!patched && opcode == Opcodes.RETURN) {
                waitingALoad1 = true;
            }
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            if(!patched && waitingALoad1 && opcode == Opcodes.ALOAD && var == 1) {
                super.visitVarInsn(Opcodes.ALOAD, 5);
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitVarInsn(Opcodes.ILOAD, 2);
                super.visitVarInsn(Opcodes.ILOAD, 3);
                super.visitVarInsn(Opcodes.ILOAD, 4);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "myessentials/event/BlockTrampleEvent", "fireEvent", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockFarmland;III)Z", false);

                Label elseLabel = new Label();
                super.visitJumpInsn(Opcodes.IFEQ, elseLabel);
                super.visitInsn(Opcodes.RETURN);
                super.visitLabel(elseLabel);
                patched = true;
            }

            super.visitVarInsn(opcode, var);
        }
    }

    @Override
    public byte[] transform(String name, String srgName, byte[] bytes) {
        if("net.minecraft.block.BlockFarmland".equals(srgName)) {
            ClassReader reader = new ClassReader(bytes);
            ClassWriter writer = new ClassWriter(reader, Opcodes.ASM4);

            ClassVisitor visitor = new ClassVisitor(Opcodes.ASM4, writer) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

                    if("func_149746_a".equals(name) || "onFallenUpon".equals(name))
                        return new FarmlandGeneratorAdapter(methodVisitor, access, name, desc);

                    return methodVisitor;
                }
            };

            reader.accept(visitor, ClassReader.EXPAND_FRAMES);

            bytes = writer.toByteArray();
        }

        return bytes;
    }
}
