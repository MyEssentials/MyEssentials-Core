package mytown.core.command;

import mytown.core.MyEssentialsCore;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class CommandManager {
    private CommandManager() {

    }

    /**
     * Map with the permission node as key and method as value.
     */
    public static final Map<String, Method> commandList = new HashMap<String, Method>();
    /**
     * Map with the permission node as key and name as value
     */
    public static final Map<String, String> commandNames = new HashMap<String, String>();
    /**
     * Map with the permission node as key and permission node of parent as value
     */
    public static final Map<String, String> commandParents = new HashMap<String, String>();
    /**
     * Map with the permission node as key and an array, that represents an ordered list of completion keys that will be used, as value
     */
    public static final Map<String, String[]> commandCompletionKeys = new HashMap<String, String[]>();
    /**
     * Map with the permission node as key and first check method as value
     */
    public static final Map<String, Method> firstPermissionBreaches = new HashMap<String, Method>();
    /**
     * Map with the completion as key and a list of available completions as value
     */
    public static final Map<String, List<String>> completionMap = new HashMap<String, List<String>>();

    /**
     * Registers all commands that are static and have the @Command or @CommandNode annotation on it.
     */
    public static void registerCommands(Class clazz) {
        registerCommands(clazz, null);
    }

    /**
     * Registers all commands that are static and have the @Command or @CommandNode annotation on it.
     */
    public static void registerCommands(Object obj) {
        registerCommands(obj.getClass(), null);
    }

    /**
     * Registers all commands that are static and have the @Command or @CommandNode annotation on it
     * and can add a custom permission check , if none is added it will use the default per-player permission system
     */
    public static void registerCommands(Object commandObj, Method firstPermissionBreach) {
        registerCommands(commandObj.getClass(), firstPermissionBreach);
    }

    /**
     * Registers all commands that are static and have the @Command or @CommandNode annotation on it
     * and can add a custom permission check , if none is added it will use the default per-player permission system
     */
    public static void registerCommands(Class clazz, Method firstPermissionBreach) {
        for (final Method m : clazz.getDeclaredMethods()) {

            if (m.isAnnotationPresent(Command.class)) {
                final Command cmd = m.getAnnotation(Command.class);
                CommandUtils.registerCommand(new CommandModel(cmd, m));

                commandList.put(cmd.permission(), m);
                commandNames.put(cmd.permission(), cmd.name());
                commandCompletionKeys.put(cmd.permission(), cmd.completionKeys());
                if(checkPermissionBreachMethod(firstPermissionBreach)) {
                    firstPermissionBreaches.put(cmd.permission(), firstPermissionBreach);
                }

            } else if(m.isAnnotationPresent(CommandNode.class)) {
                final CommandNode cmd = m.getAnnotation(CommandNode.class);
                if(hasSubCommand(cmd.name(), cmd.parentName()))
                    throw new CommandException("Sub-command with name " + cmd.name() + " and parent " + cmd.parentName() + " is registered twice!");

                commandList.put(cmd.permission(), m);
                commandNames.put(cmd.permission(), cmd.name());
                commandParents.put(cmd.permission(), cmd.parentName());
                commandCompletionKeys.put(cmd.permission(), cmd.completionKeys());
                if (checkPermissionBreachMethod(firstPermissionBreach)) {
                    firstPermissionBreaches.put(cmd.permission(), firstPermissionBreach);
                }
            }
        }
    }

    /**
     * Command call on the method with the permission node specified
     */
    public static void commandCall(String permission, ICommandSender sender, List<String> args) {
        Method m = commandList.get(permission);
        if(m == null) {
            MyEssentialsCore.Instance.LOG.error("Command with permission node " + permission + " does not exist. Aborting call.");
            return;
        }

        // Check if sender is allowed to use this command
        CommandNode cmdAnnot = commandList.get(permission).getAnnotation(CommandNode.class);
        if(cmdAnnot != null &&
                !cmdAnnot.players() && sender instanceof EntityPlayer ||
                (!cmdAnnot.nonPlayers() && sender instanceof MinecraftServer) ||
                (!cmdAnnot.nonPlayers() && sender instanceof RConConsoleSource) ||
                (!cmdAnnot.nonPlayers() && sender instanceof CommandBlockLogic)) {
            throw new CommandException("commands.generic.permission");
        }


        // Check if the player has access to the command using the firstpermissionbreach method first
        Method permMethod = firstPermissionBreaches.get(permission);
        if(permMethod != null) {
            Boolean result = true;
            try {
                result = (Boolean)permMethod.invoke(null, permission, sender);
            } catch (Exception e) {
                MyEssentialsCore.Instance.LOG.error(ExceptionUtils.getFullStackTrace(e));
            }
            if(!result) {
                // If the first permission breach did not allow the method to be called then call is aborted
                throw new CommandException("commands.generic.permission");
            }
        }

        try {
            try {
                m.invoke(null, sender, args);
            } catch (IllegalArgumentException e) {
                // If it doesn't have 2 args then it might require the subcommands
                m.invoke(null, sender, args, getSubCommandsList(permission));
            }
        } catch (InvocationTargetException e) {
            // Forced, since the functions are only gonna throw that... I think
            // TODO: Maybe have a list of all types of exceptions that could be thrown?
            throw (RuntimeException) e.getTargetException();
        } catch (Exception e2) {
            MyEssentialsCore.Instance.LOG.error(ExceptionUtils.getFullStackTrace(e2));
        }
    }

    /**
     * Gets the subCommands from a method with the @Command or @CommandNode annotation
     */
    @SuppressWarnings("unchecked")
    public static List<String> getSubCommandsList(String permission) {
        List<String> subCommands = new ArrayList<String>();

        for(String s : commandParents.keySet()) {
            if(commandParents.get(s).equals(permission)) {
                subCommands.add(s);
            }
        }

        return subCommands;
    }

    /**
     * Returns the parent's permission node or null if annotation not found
     */
    public static String getParentPermNode(String childPermNode) {
        Method m = commandList.get(childPermNode);
        if(m.isAnnotationPresent(CommandNode.class)) {
            return m.getAnnotation(CommandNode.class).parentName();
        } else {
            return null;
        }
    }

    /**
     * Gets the tab completion list for the specified arguments and the BASE permission node (from the base command)
     */
    public static List<String> getTabCompletionList(ICommandSender sender, List<String> args, String permission) {
        String perm = getPermissionNodeFromArgs(args.subList(0, args.size() - 1), permission);
        if(commandCompletionKeys.get(perm)[0].equals("")) {
            List<String> subCommands = new ArrayList<String>();
            for(String p : CommandManager.getSubCommandsList(perm)) {
                subCommands.add(CommandManager.commandNames.get(p));
            }
            List<String> completion = new ArrayList<String>();
            for(String p : subCommands) {
                if(p.toLowerCase().startsWith(args.get(args.size() - 1).toLowerCase())) {
                    completion.add(p);
                }
            }
            return completion;
        } else {
            int argNumber = 0;
            for(int i = args.size() - 1; i >= 0; i--) {
                if(!isArgumentCommand(args, i, permission)) {
                    argNumber++;
                } else {
                    break;
                }
            }
            // Because reasons
            argNumber--;
            List<String> completion = new ArrayList<String>();

            // Check if people try to tab on something that doesn't have any more arguments
            if(commandCompletionKeys.get(perm).length <= argNumber)
                return completion;

            for(String p : completionMap.get(commandCompletionKeys.get(perm)[argNumber])) {
                if(p.toLowerCase().startsWith(args.get(args.size() - 1).toLowerCase())) {
                    completion.add(p);
                }
            }
            return completion;
        }
    }

    /**
     * Gets the permission node for the actual arguments, returns back base permission node if nothing found
     */
    public static String getPermissionNodeFromArgs(List<String> args, String permissionBase) {
        // If no arguments then it's gonna return the given perm
        Iterator<String> it = args.iterator();
        String baseNode = permissionBase;
        while(it.hasNext()) {
            String subName = it.next();
            for(String perm : getSubCommandsList(baseNode)) {
                if(commandNames.get(perm).equals(subName)) {
                    baseNode = perm;
                    break;
                }
            }
        }
        return baseNode;
    }

    /**
     * Checks if the argument is an actual command rather than a parameter
     */
    @SuppressWarnings("RedundantIfStatement")
    public static boolean isArgumentCommand(List<String> args, int argNumber, String permission) {
        if(!commandNames.values().contains(args.get(argNumber))) {
            return false;
        }
        // If the command node found on the argument specified and the one behind is the same then it can't be a command
        if(getPermissionNodeFromArgs(args.subList(0, argNumber + 1), permission)
                .equals(getPermissionNodeFromArgs(args.subList(0, argNumber), permission))) {
            return false;
        }
        return true;
    }

    /**
     * Gets the node of a with name subCommand and parent's node.
     */
    public static String getSubCommandNode(String subCommand, String node) {
        for(String s : getSubCommandsList(node)) {
            if (commandNames.get(s).equals(subCommand))
                return s;
        }
        return null;
    }

    /**
     * Checks if the command with name subCommand exists as a child command of the node
     */
    public static boolean hasSubCommand(String subCommand, String node) {
        for(String s : getSubCommandsList(node)) {
            if(commandNames.get(s).equals(subCommand))
                return true;
        }
        return false;
    }

    /**
     * Checks if the permission method sent is actually valid
     */
    private static boolean checkPermissionBreachMethod(Method method) {
        return method != null && Modifier.isStatic(method.getModifiers());
    }





}
