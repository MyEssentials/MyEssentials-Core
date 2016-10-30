package myessentials.entities.api.sign;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * If a sign is created, registering it here will route all the needed event interactions to it.
 */
public class SignManager {

    public static final SignManager instance = new SignManager();

    public final Map<String, SignType> signTypes = new HashMap<String, SignType>(1);

    public Sign loadSign(World world, BlockPos bp) {
        TileEntity tileEntity = world.getTileEntity(bp);
        if(!(tileEntity instanceof TileEntitySign))
            return null;

        NBTTagCompound tagCompound = SignClassTransformer.getMyEssentialsDataValue(tileEntity);
        if(tagCompound == null)
            return null;

        SignType signType = signTypes.get(tagCompound.getString("Type"));
        if(signType == null)
            return null;

        return signType.loadData((TileEntitySign) tileEntity, tagCompound.getTag("Value"));
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock ev) {
        Sign sign = loadSign(ev.getWorld(), ev.getPos());
        if(sign == null)
            return;

        if(ev.getEntityPlayer().isSneaking()) {
            sign.onShiftRightClick(ev.getEntityPlayer());
        } else {
            sign.onRightClick(ev.getEntityPlayer());
        }
    }

    @SubscribeEvent
    public void onPlayerBreaksBlock(BlockEvent.BreakEvent ev) {
        Sign sign = loadSign(ev.getWorld(), ev.getPos());
        if(sign != null) {
            sign.onShiftRightClick(ev.getPlayer());
            ev.setCanceled(true);
        }
    }
}
