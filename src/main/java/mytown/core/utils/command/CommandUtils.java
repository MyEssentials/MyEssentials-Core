package mytown.core.utils.command;

import com.esotericsoftware.reflectasm.MethodAccess;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;

public class CommandUtils {
    private static boolean isInit = false;
    private static CommandHandler commandHandler;
    private static MethodAccess access;
    private static int method = -1;

    private static void init() {
        if (isInit) return;

        CommandUtils.commandHandler = (CommandHandler) MinecraftServer.getServer().getCommandManager();
        CommandUtils.access = MethodAccess.get(CommandHandler.class);
        try {
            CommandUtils.method = CommandUtils.access.getIndex("registerCommand", ICommand.class, String.class);
        } catch (Exception e) {
            CommandUtils.method = -1;
        }

        isInit = true;
    }

    public static void registerCommand(ICommand command, String permNode, boolean enabled) {
        init();

        if (!enabled || command == null)
            return;
        if (permNode.trim().isEmpty()) {
            permNode = command.getClass().getName();
        }

        if (CommandUtils.method == -1) {
            CommandUtils.commandHandler.registerCommand(command);
        } else {
            CommandUtils.access.invoke(CommandUtils.commandHandler, CommandUtils.method, command, permNode);
        }
    }

    public static void registerCommand(ICommand command, boolean enabled) {
        if (command == null) return;
        String permNode;
        if (command.getClass().isAnnotationPresent(Command.class)) {
            permNode = command.getClass().getAnnotation(Command.class).permission();
        } else {
            permNode = command.getClass().getName();
        }
        registerCommand(command, permNode, enabled);
    }

    public static void registerCommand(ICommand command) {
        registerCommand(command, true);
    }
}
