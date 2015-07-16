package myessentials.command.registrar;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;

/**
 * Standard vanilla command registrar
 */
public class VanillaCommandRegistrar implements ICommandRegistrar {
    protected CommandHandler commandHandler;

    public VanillaCommandRegistrar() {
        this.commandHandler = (CommandHandler) MinecraftServer.getServer().getCommandManager();
    }

    @Override
    public void registerCommand(ICommand cmd, String permNode, boolean defaultPerm) {
        this.commandHandler.registerCommand(cmd);
    }
}
