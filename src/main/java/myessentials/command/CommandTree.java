package myessentials.command;

import myessentials.entities.Tree;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class CommandTree extends Tree<CommandTreeNode> {

    public CommandTree(CommandTreeNode root) {
        super(root);
    }

    public void commandCall(ICommandSender sender, List<String> args) {
        CommandTreeNode node = getNodeFromArgs(args);
        node.commandCall(sender, args);
    }

    public CommandTreeNode getNodeFromArgs(List<String> args) {
        CommandTreeNode child = getRoot();
        while(!args.isEmpty() && child.getChild(args.get(0)) != null) {
            child = child.getChild(args.get(0));
            args = args.subList(1, args.size());
        }
        return child;
    }
}
