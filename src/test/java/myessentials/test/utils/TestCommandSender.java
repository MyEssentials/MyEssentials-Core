package myessentials.test.utils;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

/**
 *
 */
public class TestCommandSender implements ICommandSender {
    @Override
    public String getCommandSenderName() {
        return "test-myessentials";
    }

    @Override
    public IChatComponent func_145748_c_() {
        return null;
    }

    @Override
    public void addChatMessage(IChatComponent p_145747_1_) {

    }

    @Override
    public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
        return false;
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return null;
    }

    @Override
    public World getEntityWorld() {
        return null;
    }
}
