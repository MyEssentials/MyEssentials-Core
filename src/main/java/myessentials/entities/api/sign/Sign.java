package myessentials.entities.api.sign;

import myessentials.classtransformers.SignClassTransformer;
import myessentials.entities.api.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A simple wrapper class of a sign block for the server side.
 */
public abstract class Sign {

    public static final String IDENTIFIER = EnumChatFormatting.DARK_BLUE.toString();

    public final SignType signType;

    protected NBTBase data;

    protected BlockPos bp;

    protected Sign(SignType signType) {
        this.signType = signType;
    }

    public abstract void onRightClick(EntityPlayer player);

    protected abstract String[] getText();

    public void onShiftRightClick(EntityPlayer player) {
    }

    public TileEntitySign createSignBlock(EntityPlayer player, BlockPos bp, int face) {
        World world = MinecraftServer.getServer().worldServerForDimension(bp.getDim());
        ForgeDirection direction = ForgeDirection.getOrientation(face);
        if(direction == ForgeDirection.DOWN || face == 1) {
            int i1 = MathHelper.floor_double((double) ((player.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
            world.setBlock(bp.getX(), bp.getY(), bp.getZ(), Blocks.standing_sign, i1, 3);
        } else {
            world.setBlock(bp.getX(), bp.getY(), bp.getZ(), Blocks.wall_sign, face, 3);
        }

        TileEntitySign te = (TileEntitySign) world.getTileEntity(bp.getX(), bp.getY(), bp.getZ());
        String[] text = Arrays.copyOf(getText(), 4);
        text[0] = IDENTIFIER + text[0];
        normalizeText(text);

        for(int i = 0; i < 4; i++) {
            te.signText[i] = text[i] == null ? "" : text[i];
        }

        NBTTagCompound rootTag = new NBTTagCompound();
        rootTag.setString("Type", signType.getTypeID());
        if(data != null)
            rootTag.setTag("Value", data);
        SignClassTransformer.setMyEssentialsDataValue(te, rootTag);

        return te;
    }

    public TileEntitySign getTileEntity() {
        World world = MinecraftServer.getServer().worldServerForDimension(bp.getDim());
        TileEntity te = world.getTileEntity(bp.getX(), bp.getY(), bp.getZ());;
        return te instanceof TileEntitySign ? (TileEntitySign) te : null;
    }

    private void normalizeText(String[] text) {
        for(int i = 0; i < 4; i++) {
            if(text[i].length() > 15) {
                text[i] = text[i].substring(0, 15);
            }
        }
    }

    public void deleteSignBlock() {
        World world = MinecraftServer.getServer().worldServerForDimension(bp.getDim());
        world.removeTileEntity(bp.getX(), bp.getY(), bp.getZ());
        world.setBlock(bp.getX(), bp.getY(), bp.getZ(), Blocks.air);
    }

    public static class Container extends ArrayList<Sign> {
        @Override
        public boolean add(Sign sign) {
            return super.add(sign);
        }

        public Sign get(BlockPos bp) {
            for(Sign sign : this) {
                if(bp.equals(sign.bp)) {
                    return sign;
                }
            }
            return null;
        }

        public boolean contains(BlockPos bp) {
            for(Sign sign : this) {
                if(bp.equals(sign.bp)) {
                    return true;
                }
            }
            return false;
        }

        public boolean remove(BlockPos bp) {
            for(Iterator<Sign> it = iterator(); it.hasNext(); ) {
                Sign sign = it.next();
                if(bp.equals(sign.bp)) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }
    }
}
