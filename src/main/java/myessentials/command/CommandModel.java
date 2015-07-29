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

    public CommandModel(Command cmd, Method method) {
        this.commandAnnot = cmd;
    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList(commandAnnot.alias());
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
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        CommandManagerNew.getTree(commandAnnot.permission()).commandCall(sender, Arrays.asList(args));
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        CommandTree tree = CommandManagerNew.getTree(commandAnnot.permission());
        CommandTreeNode node = tree.getNodeFromArgs(Arrays.asList(args));

        int argumentNumber = tree.getArgumentNumber(Arrays.asList(args));
        if(argumentNumber < 0)
            return null;

        return node.getTabCompletionList(argumentNumber, args[args.length - 1]);
    }

    /**
     * This method does not have enough arguments to check for subcommands down the command trees therefore it always returns true.
     * The check is moved directly to the processCommand method.
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
