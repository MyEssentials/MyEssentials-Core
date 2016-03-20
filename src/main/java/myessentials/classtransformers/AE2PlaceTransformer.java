package myessentials.classtransformers;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * Patches PacketPartPlacement to add a hook for the {@link myessentials.event.AE2PartPlaceEvent}.
 * <br/>
 * The final code would be:
 * <pre><code>
 *     public class PacketPartPlacement extends AppEngPacket{
 *         // ... original fields and methods
 *
 *         public void serverPacketData(INetworkInfo manager, AppEngPacket packet, EntityPlayer player){
 *             if(PlayerUtils.convertAE2PlaceEvent(player, x, y, z, face))
 *               return;
 *             // ... original code
 *         }
 *     }
 * </code></pre>
 */
public class AE2PlaceTransformer implements IClassTransformer {

    /**
     * Generates code before the first ALOAD 3.<br>
     * The code that is generated is:
     * <pre><code>if(PlayerUtils.convertAE2PlaceEvent(player, x, y, z, face)) return;</code></pre>
     */
    private class AE2PlaceGeneratorAdapter extends GeneratorAdapter {
        boolean patched = false;

        protected AE2PlaceGeneratorAdapter(MethodVisitor mv, int access, String name, String desc) {
            super(Opcodes.ASM4, mv, access, name, desc);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            if(!patched && opcode == Opcodes.ALOAD && var == 3) {
                super.visitVarInsn(Opcodes.ALOAD, 3);
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitFieldInsn(Opcodes.GETFIELD, "appeng/core/sync/packets/PacketPartPlacement", "x", "I");
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitFieldInsn(Opcodes.GETFIELD, "appeng/core/sync/packets/PacketPartPlacement", "y", "I");
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitFieldInsn(Opcodes.GETFIELD, "appeng/core/sync/packets/PacketPartPlacement", "z", "I");
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitFieldInsn(Opcodes.GETFIELD, "appeng/core/sync/packets/PacketPartPlacement", "face", "I");
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "myessentials/event/AE2PartPlaceEvent", "fireEvent", "(Lnet/minecraft/entity/player/EntityPlayer;IIII)Z", false);

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
        if("appeng.core.sync.packets.PacketPartPlacement".equals(srgName)) {
            ClassReader reader = new ClassReader(bytes);
            ClassWriter writer = new ClassWriter(reader, Opcodes.ASM4);

            ClassVisitor visitor = new ClassVisitor(Opcodes.ASM4, writer) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

                    if("serverPacketData".equals(name))
                        return new AE2PlaceGeneratorAdapter(methodVisitor, access, name, desc);

                    return methodVisitor;
                }
            };

            reader.accept(visitor, ClassReader.EXPAND_FRAMES);

            bytes = writer.toByteArray();
        }

        return bytes;
    }
}
