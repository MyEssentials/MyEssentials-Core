package myessentials.command.registrar;

import net.minecraft.command.ICommand;

public interface ICommandRegistrar {
    /**
     * Registers an ICommand with the given permission node and default permission value
     */
    void registerCommand(ICommand cmd, String permNode, boolean defaultPerm);
}