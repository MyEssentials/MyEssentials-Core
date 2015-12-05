package myessentials.classtransformers;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.world.World;

import java.io.FileOutputStream;
import java.io.IOException;


import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * Patches BlockFire to add a hook for the {@link myessentials.event.FireSpreadEvent}.
 * <br/>
 * The final code would be:
 * <pre><code>public class BlockFire extends Block {
 *         // ... original fields and methods
 *         public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
 *             // ... original code
 *             if(FireSpreadEvent.fireEvent(i1, k1, j1, this, k2, 3)) {
 *               p_149674_1_.setBlock(i1, k1, j1, this, k2, 3);
 *             }
 *             // ... original code
 *         }
 *         // ... original methods
 *     }
 * </code></pre>
 */
public class BlockFireTransformer implements IClassTransformer {

    /**
     * Generates code on the first frame that comes after the first RETURN.<br>
     * The code that is generated is:
     * <pre><code>if(FireSpreadEvent.fireEvent(i1, k1, j1, this, k2, 3)) p_149674_1_.setBlock(i1, k1, j1, this, k2, 3);</code></pre>
     */
    private class BlockFireGeneratorAdapter extends GeneratorAdapter {
        boolean patched = false;

        protected BlockFireGeneratorAdapter(MethodVisitor mv, int access, String name, String desc) {
            super(Opcodes.ASM4, mv, access, name, desc);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (!patched && opcode == Opcodes.INVOKEVIRTUAL && owner.equals("net/minecraft/world/World") && (name.equals("func_147465_d") || name.equals("setBlock"))) {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "myessentials/event/FireSpreadEvent", "checkAndSetFire", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;II)Z", false);
                patched = true;
            } else {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
            }
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

                    if("func_149674_a".equals(name) || "updateTick".equals(name))
                        return new BlockFireGeneratorAdapter(methodVisitor, access, name, desc);

                    return methodVisitor;
                }
            };

            reader.accept(visitor, ClassReader.EXPAND_FRAMES);

            bytes = writer.toByteArray();

            // This is NOT NEEDED, I'm dumping the class for testing proposes since this is an example mod
            // This will save the modified class to a file, so you can decompile it to compare with the original version
            FileOutputStream fos = null;
            try
            {
                fos = new FileOutputStream(srgName+".class");
                fos.write(bytes);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(fos != null)
                    try
                    {
                        fos.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
            }
        }

        return bytes;
    }
}
