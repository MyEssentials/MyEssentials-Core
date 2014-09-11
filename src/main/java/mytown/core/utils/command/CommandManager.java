package mytown.core.utils.command;

import mytown.core.MyTownCore;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import scala.actors.threadpool.Arrays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class CommandManager {
    public static Map<String, Method> commandList = new HashMap<String, Method>();
    public static Map<String, String> commandNames = new HashMap<String, String>();
    public static Map<String, String> commandParents = new HashMap<String, String>();
    public static Map<String, String[]> commandCompletionKeys = new HashMap<String, String[]>();
    public static Map<String, Method> firstPermissionBreaches = new HashMap<String, Method>();

    /**
     * Registers all commands that are static and have the @Command or @CommandNode annotation on it.
     *
     *
     * @param clazz
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
     *
     * @param commandObj
     * @param firstPermissionBreach
     */
    public static void registerCommands(Object commandObj, Method firstPermissionBreach) {
        registerCommands(commandObj.getClass(), firstPermissionBreach);
    }

    /**
     * Registers all commands that are static and have the @Command or @CommandNode annotation on it
     * and can add a custom permission check , if none is added it will use the default per-player permission system
     *
     * @param clazz
     * @param firstPermissionBreach
     */
    public static void registerCommands(Class clazz, Method firstPermissionBreach) {
        for (final Method m : clazz.getDeclaredMethods()) {

            if (m.isAnnotationPresent(Command.class)) {
                final Command cmd = m.getAnnotation(Command.class);
                CommandUtils.registerCommand(new CommandModel(cmd, m)); // TODO Sub-command system

                commandList.put(cmd.permission(), m);
                commandNames.put(cmd.permission(), cmd.name());
                commandCompletionKeys.put(cmd.permission(), cmd.completionKeys());
                if(checkPermissionBreachMethod(firstPermissionBreach)) {
                    firstPermissionBreaches.put(cmd.permission(), firstPermissionBreach);
                }

            } else if(m.isAnnotationPresent(CommandNode.class)) {
                final CommandNode cmd = m.getAnnotation(CommandNode.class);

                commandList.put(cmd.permission(), m);
                commandNames.put(cmd.permission(), cmd.name());
                commandParents.put(cmd.permission(), cmd.parentName());
                commandCompletionKeys.put(cmd.permission(), cmd.completionKeys());
                if(checkPermissionBreachMethod(firstPermissionBreach)) {
                    firstPermissionBreaches.put(cmd.permission(), firstPermissionBreach);
                }
            }
        }
    }

    /**
     * Command call on the method with the permission node specified
     *
     * @param permission
     * @param sender
     * @param args
     */
    public static void commandCall(String permission, ICommandSender sender, List<String> args) {
        Method m = commandList.get(permission);
        if(m == null) {
            MyTownCore.Instance.log.error("Command with permission node " + permission + " does not exist. Aborting call.");
            return;
        }

        // Check if the player has access to the command using the firstpermissionbreach method first
        Method permMethod = firstPermissionBreaches.get(permission);
        if(permMethod != null) {
            Boolean result = true;
            try {
                // Everything is okay, don't worry lol
                result = (Boolean)permMethod.invoke(null, permission, sender);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(!result) {
                // If the first permission breach did not allow the method to be called then call is aborted
                throw new CommandException("commands.generic.permission");
            }
        }

        // Try it twice like a gangsta
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
            e2.printStackTrace();
        }
    }

    /**
     * Gets the subCommands from a method with the @Command or @CommandNode annotation
     *
     * @param permission
     * @return Returns a list if the annotation is found, else it returns null
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
     *
     * @param childPermNode
     * @return
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
     *
     * @param sender
     * @param args
     * @param permission
     * @return
     */
    public static List<String> getTabCompletionList(ICommandSender sender, List<String> args, String permission) {
        String perm = getPermissionNodeFromArgs(args.subList(0, args.size() - 1), permission);
        if(commandCompletionKeys.get(perm)[0].equals("")) {
            List<String> completion = new ArrayList<String>();
            for(String p : CommandManager.getSubCommandsList(perm)) {
                completion.add(CommandManager.commandNames.get(p));
            }
            List<String> completion2 = new ArrayList<String>();
            MyTownCore.Instance.log.info("Searching completion for : " + args.get(args.size() - 1));
            for(String p : completion) {
                if(p.startsWith(args.get(args.size() - 1))) {
                    completion2.add(p);
                }
            }
            return completion2;
        } else {
            int argNumber = 0;
            for(int i = args.size() - 1; i >= 0; i--) {
                if(!isArgumentCommand(args, i, permission)) {
                    MyTownCore.Instance.log.info("Argument " + args.get(i) + " is not a command.");
                    argNumber++;
                } else {
                    MyTownCore.Instance.log.info("Argument " + args.get(i) + " is a command... breaking");
                    break;
                }
            }
            // Because reasons
            argNumber--;
            // Check if people try to tab on something that doesn't have any more arguments
            if(commandCompletionKeys.get(perm).length <= argNumber)
                return null;
            MyTownCore.Instance.log.info("Found key: " + commandCompletionKeys.get(perm)[argNumber]);
            return CommandCompletion.completionMap.get(commandCompletionKeys.get(perm)[argNumber]);
        }
    }

    /**
     * Gets the permission node for the actual arguments, returns back base permission node if nothing found
     *
     * @param args
     * @param permissionBase
     * @return
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
     *
     * @param args
     * @param argNumber
     * @param permission
     * @return
     */
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
     * Checks if the permission method sent is actually valid
     *
     * @param method
     * @return
     */
    private static boolean checkPermissionBreachMethod(Method method) {
        return method != null && Modifier.isStatic(method.getModifiers());// && method.getReturnType() == Boolean.class && method.getParameterTypes()[0] == String.class && method.getParameterTypes()[0] == ICommandSender.class;
    }





}
