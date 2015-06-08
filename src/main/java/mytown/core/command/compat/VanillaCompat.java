package mytown.core.command.compat;

import mytown.core.command.ICommandRegistrar;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;

/**
 * Standard vanilla command registrar
 *
 * @author Joe Goett
 */
public class VanillaCompat extends ICommandRegistrar {
    protected CommandHandler commandHandler;

    public VanillaCompat() {
        this.commandHandler = (CommandHandler) MinecraftServer.getServer().getCommandManager();
    }

    @Override
    public void registerCommand(ICommand cmd, String permNode, boolean defaultPerm) {
        this.commandHandler.registerCommand(cmd);
    }
}
