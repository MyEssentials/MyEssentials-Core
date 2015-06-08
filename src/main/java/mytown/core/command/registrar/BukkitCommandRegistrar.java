package mytown.core.command.registrar;

import mytown.core.MyEssentialsCore;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Method;

/**
 * Bukkit (Cauldron/MCPC) command registrar.
 *
 * NOTE: Falls back to vanilla if fails to register command or can't find the register command.
 */
public class BukkitCommandRegistrar extends VanillaCommandRegistrar {
    private Method registerCmdMethod = null;

    public BukkitCommandRegistrar() {
        super();
        try {
            registerCmdMethod = CommandHandler.class.getMethod("registerCommand", ICommand.class, String.class);
        } catch (NoSuchMethodException e) {
            MyEssentialsCore.instance.LOG.error("Could not find registerCommand method with initializing command registrar.");
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public void registerCommand(ICommand cmd, String permNode, boolean defaultPerm) {
        try {
            registerCmdMethod.invoke(this.commandHandler, cmd, permNode);
        } catch (Exception e) {
            MyEssentialsCore.instance.LOG.error("Failed to register command (BukkitCompat) Falling back to vanilla command registrar.");
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(e));
            super.registerCommand(cmd, permNode, defaultPerm);
        }
    }
}
