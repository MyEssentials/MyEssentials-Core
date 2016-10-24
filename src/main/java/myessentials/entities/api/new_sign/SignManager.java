package myessentials.entities.api.new_sign;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class SignManager {

    public static void register(Location<World> pos, Direction dir) {
        Sign sign;
        if(pos.getBlockType() == BlockTypes.AIR) {

            BlockType type;
            if (dir == Direction.DOWN) {
                type = BlockTypes.STANDING_SIGN;
            } else {
                type = BlockTypes.WALL_SIGN;
            }
            BlockState state = BlockState.builder().blockType(type).build();
            pos.getExtent().setBlock(pos.getBlockPosition(), state);

            sign = (Sign) pos.getTileEntity().get();
            sign.getOrCreate(ShopSignData.class);

        } else {
            System.err.println("There was an error when creating the sign");
        }
    }

    @Listener
    public void onInteract(InteractBlockEvent ev) {
        Optional<TileEntity> te = ev.getTargetBlock().getLocation().get().getTileEntity();
        if (te.isPresent() && te.get() instanceof Sign) {
            Optional<ShopSignData> signData = te.get().get(ShopSignData.class);
        }
    }

}
