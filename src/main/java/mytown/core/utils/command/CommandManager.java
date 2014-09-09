package mytown.core.utils.command;

import mytown.core.MyTownCore;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import scala.actors.threadpool.Arrays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    public static Map<String, Method> commandList = new HashMap<String, Method>();
    public static Map<String, String> commandNames = new HashMap<String, String>();
    public static Map<String, String> commandParents = new HashMap<String, String>();
    public static Map<String, Method> firstPermissionBreaches = new HashMap<String, Method>();

    public static void registerCommands(Class clazz) {
        registerCommands(clazz, null);
    }

    public static void registerCommands(Object obj) {
        registerCommands(obj.getClass(), null);
    }

    public static void registerCommands(Object commandObj, Method firstPermissionBreach) {
        registerCommands(commandObj.getClass(), firstPermissionBreach);
    }

    public static void registerCommands(Class clazz, Method firstPermissionBreach) {
        for (final Method m : clazz.getDeclaredMethods()) {

            if (m.isAnnotationPresent(Command.class)) {
                final Command cmd = m.getAnnotation(Command.class);
                CommandUtils.registerCommand(new CommandModel(cmd, m)); // TODO Sub-command system

                commandList.put(cmd.permission(), m);
                commandNames.put(cmd.permission(), cmd.name());
                if(checkPermissionBreachMethod(firstPermissionBreach)) {
                    MyTownCore.Instance.log.info("Found permission breach method... adding");
                    firstPermissionBreaches.put(cmd.permission(), firstPermissionBreach);
                }

            } else if(m.isAnnotationPresent(CommandNode.class)) {
                final CommandNode cmd = m.getAnnotation(CommandNode.class);

                commandList.put(cmd.permission(), m);
                commandNames.put(cmd.permission(), cmd.name());
                commandParents.put(cmd.permission(), cmd.parentName());
                if(checkPermissionBreachMethod(firstPermissionBreach)) {
                    MyTownCore.Instance.log.info("Found permission breach method... adding");
                    firstPermissionBreaches.put(cmd.permission(), firstPermissionBreach);
                }
            }
        }
    }



    public static void commandCall(String permission, ICommandSender sender, List<String> args) {
        Method m = commandList.get(permission);
        if(m == null) {
            MyTownCore.Instance.log.error("Command with permission node " + permission + " does not exist. Aborting call.");
            return;
        }

        // Check if the player has access to the command using the firstpermissionbreach method first
        Method permMethod = firstPermissionBreaches.get(permission);
        if(permMethod != null) {
            MyTownCore.Instance.log.info("Found permission breach method... calling");
            try {
                // Everything is okay, don't worry lol
                Boolean result = (Boolean)permMethod.invoke(null, permission, sender);
                if(!result) {
                    // If the first permission breach did not allow the method to be called then call is aborted

                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
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
     * Checks if the permission method sent is actually valid
     *
     * @param method
     * @return
     */
    private static boolean checkPermissionBreachMethod(Method method) {
        MyTownCore.Instance.log.info("Checking method...");
        return method != null && Modifier.isStatic(method.getModifiers());// && method.getReturnType() == Boolean.class && method.getParameterTypes()[0] == String.class && method.getParameterTypes()[0] == ICommandSender.class;
    }



}
