package myessentials.command;

import myessentials.MyEssentialsCore;
import myessentials.command.annotation.Command;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Command model which instantiates all base commands that need to be registered to Minecraft
 */
public class CommandModel extends CommandBase {
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

    public String getPermissionNode() {
        return commandAnnot.permission();
    }

    public boolean canConsoleUseCommand() {
        return commandAnnot.nonPlayers();
    }

    public boolean canRConUseCommand() {
        return commandAnnot.nonPlayers();
    }

    public boolean canCommandBlockUseCommand() {
        return commandAnnot.nonPlayers();
    }

    public String getCommandName() {
        return commandAnnot.name();
    }

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
        /*
        if(sender instanceof EntityPlayer && commandAnnot.opsOnlyAccess() && !PlayerUtils.isOp((EntityPlayer) sender))
            throw new CommandException("commands.generic.permission");
        */
        CommandManagerNew.getTree(commandAnnot.permission()).commandCall(sender, Arrays.asList(args));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        CommandTree tree = CommandManagerNew.getTree(commandAnnot.permission());
        CommandTreeNode node = tree.getNodeFromArgs(Arrays.asList(args));

        int argumentNumber = tree.getArgumentNumber(Arrays.asList(args));
        if(argumentNumber < 0)
            return null;

        return node.getTabCompletionList(argumentNumber, args[args.length - 1]);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        /*
        if(sender instanceof EntityPlayer && commandAnnot.opsOnlyAccess() && !MinecraftServer.getServer().getConfigurationManager().func_152607_e(((EntityPlayer) sender).getGameProfile()))
            return false;
        */
        return true;
    }
}
