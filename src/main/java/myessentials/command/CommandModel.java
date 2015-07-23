package myessentials.command;

import cpw.mods.fml.common.Optional;
import myessentials.utils.PlayerUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.permission.PermissionLevel;
import net.minecraftforge.permission.PermissionObject;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Command model which instantiates all base commands that need to be registered to Minecraft
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "net.minecraftforge.permission.PermissionObject", modid = "ForgeEssentials")
})
public class CommandModel extends CmdBase implements PermissionObject {
    /**
     * The Command annotation which holds information about the node's position.
     */
    private final Command commandAnnot;

    /**
     * List which retains all aliases for ease of use
     */
    private List commandAliasCache = null;

    public CommandModel(Command cmd, Method method) {
        this.commandAnnot = cmd;
    }

    @Override
    public List getCommandAliases() {
        if (commandAliasCache == null) {
            commandAliasCache = Arrays.asList(commandAnnot.alias());
        }
        return commandAliasCache;
    }

    @Override
    public String getPermissionNode() {
        return commandAnnot.permission();
    }

    @Override
    @Optional.Method(modid = "ForgeEssentials")
    public PermissionLevel getPermissionLevel() {
        if (commandAnnot.opsOnlyAccess()) {
            return PermissionLevel.OP;
        }

        return PermissionLevel.FALSE;
    }

    @Override
    public boolean canConsoleUseCommand() {
        return commandAnnot.console();
    }

    @Override
    public boolean canRConUseCommand() {
        return commandAnnot.rcon();
    }

    @Override
    public boolean canCommandBlockUseCommand() {
        return commandAnnot.commandblocks();
    }

    @Override
    public String getCommandName() {
        return commandAnnot.name();
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + commandAnnot.name() + " " + commandAnnot.syntax();
    }

    /**
     * Processes the command by calling the method that was linked to it.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        // Return if player is not allowed to use this command
        if(sender instanceof EntityPlayer && commandAnnot.opsOnlyAccess() && !PlayerUtils.isOp((EntityPlayer) sender))
            throw new CommandException("commands.generic.permission");

        CommandManager.commandCall(getPermissionNode(), sender, Arrays.asList(args));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandManager.getTabCompletionList(sender, Arrays.asList(args), commandAnnot.permission());
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        if(sender instanceof EntityPlayer && commandAnnot.opsOnlyAccess() && !MinecraftServer.getServer().getConfigurationManager().func_152607_e(((EntityPlayer) sender).getGameProfile()))
            return false;

        return true;
    }
}
