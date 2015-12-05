package myessentials.classtransformers;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.world.World;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * Patches BlockFire to add a hook for the {@link myessentials.event.ModifyBlockEvent}.
 * <br/>
 * For the method updateTick and tryCatchFire:
 * <pre><code>world.setBlock(x, y, z, block, meta, flags) is replaced by
 * ModifyBlockEvent.checkAndSetBlock(world, x, y, z, block, meta, flags)</code></pre>
 *
 * For the method tryCatchFire:
 *  <pre><code>world.setBlockToAir(x, y, z) is replaced by
 *  ModifyBlockEvent.checkAndSetBlockToAir(world, x, y, z)</code></pre>
 *
 */
public class BlockFireTransformer implements IClassTransformer {

    private class BlockFireGeneratorAdapter extends GeneratorAdapter {
        boolean kcauldronDetected = false;
        private String methodTransformed;

        protected BlockFireGeneratorAdapter(MethodVisitor mv, int access, String name, String desc) {
            super(Opcodes.ASM4, mv, access, name, desc);
            this.methodTransformed = name;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if ((methodTransformed.equals("func_149674_a") || methodTransformed.equals("updateTick")) && !kcauldronDetected && opcode == Opcodes.INVOKEVIRTUAL) {
                if (owner.equals("net/minecraft/world/World") && name.equals("func_147465_d") || name.equals("setBlock")) {
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, "myessentials/event/ModifyBlockEvent", "checkAndSetBlock", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;II)Z", false);
                    return;
                } else if (owner.equals("org/bukkit/event/block/BlockIgniteEvent") && name.equals("isCancelled")) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    super.visitVarInsn(Opcodes.ALOAD, 1);
                    super.visitVarInsn(Opcodes.ILOAD, 10);
                    super.visitVarInsn(Opcodes.ILOAD, 12);
                    super.visitVarInsn(Opcodes.ILOAD, 11);
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, "myessentials/event/ModifyBlockEvent", "checkFlagAndBlock", "(ZLnet/minecraft/world/World;III)Z", false);
                    kcauldronDetected = true;
                    return;
                }
            } else if (methodTransformed.equals("tryCatchFire") && opcode == Opcodes.INVOKEVIRTUAL && owner.equals("net/minecraft/world/World")) {
                if (name.equals("func_147465_d") || name.equals("setBlock")) {
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, "myessentials/event/ModifyBlockEvent", "checkAndSetBlock", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;II)Z", false);
                    return;
                } else if (name.equals("func_147468_f") || name.equals("setBlockToAir")) {
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, "myessentials/event/ModifyBlockEvent", "checkAndSetBlockToAir", "(Lnet/minecraft/world/World;III)Z", false);
                    return;
                }
            }

            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

    @Override
    public byte[] transform(String name, String srgName, byte[] bytes) {
        if("net.minecraft.block.BlockFire".equals(srgName)) {
            ClassReader reader = new ClassReader(bytes);
            ClassWriter writer = new ClassWriter(reader, Opcodes.ASM4);

            ClassVisitor visitor = new ClassVisitor(Opcodes.ASM4, writer) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

                    if("func_149674_a".equals(name) || "updateTick".equals(name) || "tryCatchFire".equals(name)) {
                        return new BlockFireGeneratorAdapter(methodVisitor, access, name, desc);
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
