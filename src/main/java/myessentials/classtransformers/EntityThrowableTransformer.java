package myessentials.classtransformers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * Patches EntityThrowable to add a hook for the {@link myessentials.event.ProjectileImpactEvent}.
 * <br/>
 * The final code would be:
 * <pre><code>public class EntityThrowable extends Entity implements IProjectile {
 *         // ... original fields and methods
 *         public void onUpdate() {
 *             // ... original code
 *             if(ProjectileImpactEvent.fireEvent(this, movingobjectposition)) {
 *               this.onImpact(movingobjectposition);
 *             }
 *             // ... original code
 *         }
 *         // ... original methods
 *     }
 * </code></pre>
 */
public class EntityThrowableTransformer implements IClassTransformer {

    /**
     * Generates code on the first frame that comes after the first RETURN.<br>
     * The code that is generated is:
     * <pre><code>if(ProjectileImpactEvent.fireEvent(this, movingobjectposition)) this.onImpact(movingobjectposition);</code></pre>
     */
    private class EntityThrowableGeneratorAdapter extends GeneratorAdapter {
        boolean patched = false;

        protected EntityThrowableGeneratorAdapter(MethodVisitor mv, int access, String name, String desc) {
            super(Opcodes.ASM4, mv, access, name, desc);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (!patched && opcode == Opcodes.INVOKEVIRTUAL && owner.equals("net/minecraft/entity/projectile/EntityThrowable") && (name.equals("func_70184_a") || name.equals("onImpact"))) {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "myessentials/event/ProjectileImpactEvent", "fireEvent", "(Lnet/minecraft/entity/projectile/EntityThrowable;Lnet/minecraft/util/MovingObjectPosition;)Z", false);
                Label elseLabel = new Label();
                super.visitJumpInsn(Opcodes.IFEQ, elseLabel);
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitVarInsn(Opcodes.ALOAD, 3);
                super.visitMethodInsn(opcode, owner, name, desc, itf);
                super.visitLabel(elseLabel);
                patched = true;
            } else {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
            }
        }
    }

    @Override
    public byte[] transform(String name, String srgName, byte[] bytes) {
        if("net.minecraft.entity.projectile.EntityThrowable".equals(srgName)) {
            ClassReader reader = new ClassReader(bytes);
            ClassWriter writer = new ClassWriter(reader, Opcodes.ASM4);

            ClassVisitor visitor = new ClassVisitor(Opcodes.ASM4, writer) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

                    if("func_70071_h_".equals(name) || "onUpdate".equals(name))
                        return new EntityThrowableGeneratorAdapter(methodVisitor, access, name, desc);

                    return methodVisitor;
                }
            };

            reader.accept(visitor, ClassReader.EXPAND_FRAMES);

            bytes = writer.toByteArray();
        }

        return bytes;
    }
}
