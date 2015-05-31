package mytown.core.utils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;

// TODO Move to ForgePerms API after the rewrite

public class Assert {
	/**
	 * Checks if command server has the given permission node. Does NOT allow console to access.
	 * 
	 * @param sender
	 * @param node
	 * @throws CommandException
	 */
	public static void Perm(ICommandSender sender, String node) throws CommandException {
		Assert.Perm(sender, node, false);
	}

	/**
	 * Checks if command sender has the given permission node.
	 * 
	 * @param sender
	 * @param node
	 * @param allowConsole
	 * @throws CommandException
	 */
	public static void Perm(ICommandSender sender, String node, boolean allowConsole) throws CommandException {
        Assert.Perm(sender, node, allowConsole, allowConsole, false);
	}

    public static void Perm(ICommandSender sender, String node, boolean allowConsole, boolean allowRCon, boolean allowCmdBlock) throws CommandException {
        if ((sender instanceof MinecraftServer && !allowConsole) || (sender instanceof RConConsoleSource && !allowRCon) || (sender instanceof CommandBlockLogic && !allowCmdBlock)) {
            throw new CommandException("commands.generic.permission");
        } else if (!(sender instanceof EntityPlayer)) {
            return;
        }

        if (node == null)
            return;
        EntityPlayer player = (EntityPlayer) sender;
        // For now not using fpapi since it's not ported yet
        //if (ForgePermsAPI.permManager.canAccess(player.getCommandSenderName(), player.worldObj.provider.getDimensionName(), node)) // TODO Make ForgePerms use UUIDs (Will need to re-write everything)
        return;
        //throw new CommandException("commands.generic.permission");
    }
}