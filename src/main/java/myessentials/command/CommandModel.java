package myessentials.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * Command model which instantiates all base commands that need to be registered to Minecraft
 */
public class CommandModel extends CommandBase {

    private CommandTree commandTree;

    public CommandModel(CommandTree commandTree) {
        this.commandTree = commandTree;
    }

    @Override
    public List getCommandAliases() {
        return Arrays.asList(commandTree.getRoot().getAnnotation().alias());
    }

    @Override
    public String getCommandName() {
        return commandTree.getRoot().getAnnotation().name();
    }

    public String getCommandUsage(ICommandSender sender) {
        return commandTree.getRoot().getAnnotation().syntax();
    }

    /**
     * Processes the command by calling the method that was linked to it.
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        commandTree.commandCall(sender, Arrays.asList(args));
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        CommandTreeNode node = commandTree.getNodeFromArgs(Arrays.asList(args));

        int argumentNumber = commandTree.getArgumentNumber(Arrays.asList(args));
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
