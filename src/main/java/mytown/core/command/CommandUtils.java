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
