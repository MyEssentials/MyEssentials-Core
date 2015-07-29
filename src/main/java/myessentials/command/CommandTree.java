package myessentials.command;

import myessentials.Localization;
import myessentials.MyEssentialsCore;
import myessentials.entities.Tree;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class CommandTree extends Tree<CommandTreeNode> {

    private Localization local;

    public CommandTree(CommandTreeNode root, Localization local) {
        super(root);
        this.local = local;
    }

    public void commandCall(ICommandSender sender, List<String> args) {
        CommandTreeNode node = getRoot();
        while (!args.isEmpty() && node.getChild(args.get(0)) != null) {
            node = node.getChild(args.get(0));
            args = args.subList(1, args.size());
        }

        node.commandCall(sender, args);
    }

    public CommandTreeNode getNodeFromArgs(List<String> args) {
        CommandTreeNode child = getRoot();
        while (!args.isEmpty() && child.getChild(args.get(0)) != null) {
            child = child.getChild(args.get(0));
            args = args.subList(1, args.size());
        }
        return child;
    }

    public int getArgumentNumber(List<String> args) {
        CommandTreeNode current = getRoot();
        while (!args.isEmpty() && current.getChild(args.get(0)) != null) {
            current = current.getChild(args.get(0));
            args = args.subList(1, args.size());
        }

        return args.size() - 1;
    }

    public boolean hasCommandNode(String perm) {
        return hasCommandNode(getRoot(), perm);
    }

    public boolean hasCommandNode(CommandTreeNode current, String perm) {
        if(perm.equals(current.getAnnotation().permission()))
            return true;

        boolean exists = false;
        for(CommandTreeNode child : current.getChildren()) {
            if(hasCommandNode(child, perm))
                return true;
        }
        return false;
    }

    public Localization getLocal() {
        return local;
    }
}
