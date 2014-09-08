package mytown.core.utils.command;

import mytown.core.MyTownCore;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import scala.actors.threadpool.Arrays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    public static Map<String, Method> commandList = new HashMap<String, Method>();
    public static Map<String, String> commandNames = new HashMap<String, String>();
    public static Map<String, String> commandParents = new HashMap<String, String>();

    public static void registerCommands(Class clazz) {
        for (final Method m : clazz.getDeclaredMethods()) {

            if (m.isAnnotationPresent(Command.class)) {
                final Command cmd = m.getAnnotation(Command.class);
                CommandUtils.registerCommand(new CommandModel(cmd, m)); // TODO Sub-command system

                commandList.put(cmd.permission(), m);
                commandNames.put(cmd.permission(), cmd.name());

            } else if(m.isAnnotationPresent(CommandNode.class)) {
                final CommandNode cmd = m.getAnnotation(CommandNode.class);

                commandList.put(cmd.permission(), m);
                commandNames.put(cmd.permission(), cmd.name());
                commandParents.put(cmd.permission(), cmd.parentName());
            }
        }
    }

    public static void registerCommands(Object commandObj) {
        registerCommands(commandObj.getClass());
    }


    public static void commandCall(String permission, ICommandSender sender, List<String> args) {
        Method m = commandList.get(permission);
        if(m == null) {
            MyTownCore.Instance.log.error("Command with permission node " + permission + " does not exist. Aborting call.");
            return;
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

        /*
        if(m.isAnnotationPresent(Command.class)) {
            return Arrays.asList(m.getAnnotation(Command.class).subCommands());
        } else if(m.isAnnotationPresent(CommandNode.class)) {
            return Arrays.asList(m.getAnnotation(CommandNode.class).subCommands());
        } else {
            return null;
        }
        */
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



}
