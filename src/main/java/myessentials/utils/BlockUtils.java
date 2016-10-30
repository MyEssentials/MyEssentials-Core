package myessentials.utils;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockUtils {
    public static String getBlockName(Block block) {
        return GameRegistry.findRegistry(Block.class).getKey(block).toString().replace(":", ".");
    }
}
