package myessentials.command.registrar;

import net.minecraft.command.ICommand;
import net.minecraftforge.permission.PermissionLevel;
import net.minecraftforge.permission.PermissionManager;

/**
 * ForgeEssentials command registrar.
 */
public class ForgeEssentialsCommandRegistrar implements ICommandRegistrar {
    @Override
    public void registerCommand(ICommand cmd, String permNode, boolean defaultPerm) {
        PermissionManager.registerPermission(permNode, PermissionLevel.fromBoolean(defaultPerm));
    }
}
