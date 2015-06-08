package mytown.core.command.compat;

import mytown.core.command.ICommandRegistrar;
import net.minecraft.command.ICommand;
import net.minecraftforge.fe.server.CommandHandlerForge;
import net.minecraftforge.permissions.PermissionsManager;

/**
 * ForgeEssentials command registrar.
 */
public class ForgeEssentialsCompat extends ICommandRegistrar {
    @Override
    public void registerCommand(ICommand cmd, String permNode, boolean defaultPerm) {
        CommandHandlerForge.registerCommand(cmd, permNode, PermissionsManager.RegisteredPermValue.fromBoolean(defaultPerm));
    }
}
