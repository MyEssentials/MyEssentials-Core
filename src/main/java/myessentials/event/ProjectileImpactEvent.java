package myessentials.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * Fired when an EntityThrowable or EntityFireball is about to call onImpact().
 * If the event is canceled onImpact() is not called.
 */
@Cancelable
public class ProjectileImpactEvent extends EntityEvent 
{
    /**
     * The entity that fires the projectile
     */
    public final EntityLivingBase firingEntity;
    
    /**
     * The MovingObjectPosition that is generated from this impact
     */
    public final MovingObjectPosition movingObjectPosition;
    
    /**
     * Creates a new event for an impacting EntityThrowable
     */
    public ProjectileImpactEvent(EntityThrowable throwable, MovingObjectPosition mop)
    {
        super(throwable);
        this.firingEntity = throwable.getThrower();
        this.movingObjectPosition = mop;
    }

    /**
     * Creates a new event for an impacting EntityFireball
     */
    public ProjectileImpactEvent(EntityFireball fireball, MovingObjectPosition mop)
    {
        super(fireball);
        this.firingEntity = fireball.shootingEntity;
        this.movingObjectPosition = mop;
    }

    @SuppressWarnings("unused")
    public static boolean fireEvent(EntityFireball fireball, MovingObjectPosition mop) {
        return !MinecraftForge.EVENT_BUS.post(new ProjectileImpactEvent(fireball, mop));
    }

    @SuppressWarnings("unused")
    public static boolean fireEvent(EntityThrowable throwable, MovingObjectPosition mop) {
        return !MinecraftForge.EVENT_BUS.post(new ProjectileImpactEvent(throwable, mop));
    }
}
