package myessentials.command;

import myessentials.Localization;
import myessentials.MyEssentialsCore;
import myessentials.chat.HelpMenu;
import myessentials.command.annotation.Command;
import myessentials.entities.TreeNode;
import myessentials.utils.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * The declaration is a bit difficult to understand.
 * It means that the super class "TreeNode" should work with other "CommandTreeNode" objects.
 */
public class CommandTreeNode extends TreeNode<CommandTreeNode> {

    private Command commandAnnot;
    private Method method;

    private HelpMenu helpMenu;

    public CommandTreeNode(Command commandAnnot, Method method) {
        this(null, commandAnnot, method);
    }

    public CommandTreeNode(CommandTreeNode parent, Command commandAnnot, Method method) {
        this.parent = parent;
        this.commandAnnot = commandAnnot;
        this.method = method;
    }

    public Command getAnnotation() {
        return commandAnnot;
    }

    public Method getMethod() {
        return method;
    }

    public void commandCall(ICommandSender sender, List<String> args) {
        if (commandAnnot != null &&
                ((!commandAnnot.players() && sender instanceof EntityPlayer) ||
                        (!commandAnnot.nonPlayers() && sender instanceof MinecraftServer) ||
                        (!commandAnnot.nonPlayers() && sender instanceof RConConsoleSource) ||
                        (!commandAnnot.nonPlayers() && sender instanceof CommandBlockLogic))) {
            throw new CommandException("commands.generic.permission");
        }


        /*
        // Check if the player has access to the command using the firstpermissionbreach method first
        Method permMethod = firstPermissionBreaches.get(permission);
        if(permMethod != null) {
            Boolean result = true;
            try {
                result = (Boolean)permMethod.invoke(null, permission, sender);
            } catch (Exception e) {
                MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(e));
            }
            if(!result) {
                // If the first permission breach did not allow the method to be called then call is aborted
                throw new CommandException("commands.generic.permission");
            }
        }
        */

        try {
            CommandResponse response = (CommandResponse)method.invoke(null, sender, args);
            if(response == CommandResponse.SEND_HELP_MESSAGE) {
                int page = 1;
                if(!args.isEmpty() && StringUtils.tryParseInt(args.get(0)))
                    page = Integer.parseInt(args.get(0));
                sendHelpMessage(sender, page);
            }
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException)
                throw (RuntimeException) e.getCause();
            else
                MyEssentialsCore.instance.LOG.info(ExceptionUtils.getStackTrace(e));
        } catch (Exception e2) {
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(e2));
        }
    }

    public List<String> getTabCompletionList(int argumentNumber) {
        List<String> completion = new ArrayList<String>();
        if(commandAnnot.completionKeys().length == 0) {
            for(CommandTreeNode child : getChildren()) {
                completion.add(child.commandAnnot.name());
            }
        } else {
            if(argumentNumber < commandAnnot.completionKeys().length) {
                completion.addAll(CommandCompletion.getCompletionList(commandAnnot.completionKeys()[argumentNumber]));
            }
        }
        return completion;
    }

    public void sendHelpMessage(ICommandSender sender, int page) {
        if(helpMenu == null)
            constructHelpMenu();
        helpMenu.sendHelpPage(sender, page);
    }

    public CommandTreeNode getChild(String name) {
        for(CommandTreeNode child : getChildren()) {
            if(child.getAnnotation().name().equals(name))
                return child;
        }
        return null;
    }

    public String getCommandLine() {
        if(getParent() == null)
            return "/" + commandAnnot.name();
        else
            return getParent().getCommandLine() + " " + commandAnnot.name();
    }

    private void constructHelpMenu() {
        String commandLine = getCommandLine();
        helpMenu = new HelpMenu(commandLine);
        if(getChildren().isEmpty()) {
            helpMenu.addLine(getLocal().getLocalization(getAnnotation().permission() + ".help"));
        } else {
            for (CommandTreeNode child : getChildren()) {
                helpMenu.addLineWithHoverText(commandLine + " " + child.getAnnotation().name(), getLocal().getLocalization(child.getAnnotation().permission() + ".help"));
            }
        }
    }

    public Localization getLocal() {
        CommandTreeNode node = this;

        while(node.getParent() != null) {
            node = node.getParent();
        }
        return CommandManagerNew.getTree(node.getAnnotation().permission()).getLocal();
    }
}
