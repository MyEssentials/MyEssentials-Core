package mytown.core.utils.command;

import java.lang.reflect.Method;

public class CommandManager {
    public static void registerCommands(Class clazz) {
        for (final Method m : clazz.getDeclaredMethods()) {
            if (!m.isAnnotationPresent(Command.class)) continue;
            final Command cmd = m.getAnnotation(Command.class);
            CommandUtils.registerCommand(new CommandModel(cmd, m)); // TODO Sub-command system
        }
    }

    public static void registerCommands(Object commandObj) {
        registerCommands(commandObj.getClass());
    }
}
