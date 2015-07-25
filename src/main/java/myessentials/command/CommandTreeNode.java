package myessentials.command;

import myessentials.Localization;
import myessentials.MyEssentialsCore;
import myessentials.chat.HelpMenu;
import myessentials.command.annotation.CommandNode;
import myessentials.entities.TreeNode;
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
 * It means that the super class "TreeNode" should only work with other "CommandTreeNode" objects.
 */
public class CommandTreeNode extends TreeNode<CommandTreeNode> {

    private CommandNode commandAnnot;
    private Method method;

    private HelpMenu helpMenu;

    public CommandTreeNode(CommandNode commandAnnot, Method method) {
        this.commandAnnot = commandAnnot;
        this.method = method;
    }

    public CommandNode getAnnotation() {
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
            method.invoke(null, sender, args);
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
                completion.addAll(CommandManagerNew.getCompletionList(commandAnnot.completionKeys()[argumentNumber]));
            }
        }
        return completion;
    }

    public void sendHelpMessage(ICommandSender sender, int page, Localization local) {
        if(helpMenu == null)
            constructHelpMenu(local);
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
            return getParent().commandAnnot.name() + commandAnnot.name();
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof String) {
            return commandAnnot.name().equals(obj);
        }

        return super.equals(obj);
    }

    private void constructHelpMenu(Localization local) {
        String commandLine = getCommandLine();
        helpMenu = new HelpMenu(commandLine);
        for(CommandTreeNode child : getChildren()) {
            helpMenu.addLineWithHoverText(getCommandLine() + " " + child.commandAnnot.name(), local.getLocalization(commandAnnot.permission() + ".help"));
        }
    }
}
