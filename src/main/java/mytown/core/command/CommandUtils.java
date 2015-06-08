package mytown.core.command;

import cpw.mods.fml.common.Loader;
import mytown.core.MyEssentialsCore;
import mytown.core.command.compat.BukkitCompat;
import mytown.core.command.compat.ForgeEssentialsCompat;
import mytown.core.command.compat.VanillaCompat;
import net.minecraft.command.ICommand;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Helpers to register commands with permission nodes.
 */
public final class CommandUtils {
    // For lazy init
    private static boolean isInit = false;

    // The command registrar
    private static ICommandRegistrar registrar;

    private static void init() {
        Class<? extends ICommandRegistrar> registrarClass;

        if (MyEssentialsCore.isMCPC) { // Bukkit Compat takes precedence
            registrarClass = BukkitCompat.class;
        } else if (Loader.isModLoaded("ForgeEssentials")) { // Then Forge Essentials
            registrarClass = ForgeEssentialsCompat.class;
        } else { // Finally revert to Vanilla (Ew, Vanilla Minecraft)
            registrarClass = VanillaCompat.class;
        }

        try {
            CommandUtils.registrar = registrarClass.newInstance();
            isInit = true;
        } catch (Exception e) {
            MyEssentialsCore.instance.LOG.error("Failed to create command registrar (%s).", registrarClass.getName());
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public static void registerCommand(ICommand command, String permNode, boolean enabled) {
        if(!isInit)
            init();

        if (!enabled || command == null)
            return;

        CommandUtils.registrar.registerCommand(command, permNode);
    }

    public static void registerCommand(ICommand command, boolean enabled) {
        if(!isInit)
            init();

        if (!enabled || command == null)
            return;

        CommandUtils.registrar.registerCommand(command);
    }

    public static void registerCommand(ICommand command) {
        if(!isInit)
            init();

        CommandUtils.registrar.registerCommand(command);
    }
}

/*
import com.esotericsoftware.reflectasm.MethodAccess;
import mytown.core.MyEssentialsCore;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class CommandUtils {

    private static boolean isInit = false;

    */
/**
     * CommandHandler of Minecraft
     *//*

    private static CommandHandler commandHandler;
    */
/**
     * MethodAccess to the CommandHandler for calling private methods
     *//*

    private static MethodAccess access;
    */
/**
     * Method index since the call occurs using indexes instead of names.
     *//*

    private static int method = -1;

    private CommandUtils() {
    }
    */
/**
     * Initializing the fields
     *//*

    private static void init() {
        try {
            CommandUtils.commandHandler = (CommandHandler) MinecraftServer.getServer().getCommandManager();
        } catch (Exception ex) {
            MyEssentialsCore.instance.LOG.info("CommandHandler could not be found.");
            MyEssentialsCore.instance.LOG.info(ExceptionUtils.getStackTrace(ex));
        }
        CommandUtils.access = MethodAccess.get(CommandHandler.class);
        try {
            CommandUtils.method = CommandUtils.access.getIndex("registerCommand", ICommand.class, String.class);
        } catch (Exception e) {
            CommandUtils.method = -1;
            MyEssentialsCore.instance.LOG.error("Could not find registerCommand method when initializing command registerer.");
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(e));
        }

        isInit = true;
    }

    */
/**
     * Register a command using its permission node.
     *//*

    public static void registerCommand(ICommand command, String permNode, boolean enabled) {
        if(!isInit)
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

    */
/**
     * Registers a command.
     *//*

    public static void registerCommand(ICommand command, boolean enabled) {
        if (command == null)
            return;
        String permNode;
        if (command.getClass().isAnnotationPresent(Command.class)) {
            permNode = command.getClass().getAnnotation(Command.class).permission();
        } else {
            permNode = command.getClass().getName();
        }
        registerCommand(command, permNode, enabled);
    }

    */
/**
     * Registers a command.
     *//*

    public static void registerCommand(ICommand command) {
        registerCommand(command, true);
    }
}
*/
