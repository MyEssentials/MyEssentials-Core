package mytown.core.command;

import net.minecraft.command.ICommand;

/**
 */
public abstract class ICommandRegistrar {
    /**
     * Registers an ICommand with the given permission node and default permission value
     *
     * @param cmd The ICommand instance to register
     * @param permNode The permission node to use
     * @param defaultPerm The default permission value (True = Can use without perm, False = Can NOT use without perm)
     */
    public abstract void registerCommand(ICommand cmd, String permNode, boolean defaultPerm);

    /**
     * Registers an ICommand with the given permission node. Otherwise, the permission is the classpath.
     *
     * @param cmd The ICommand instance to register
     * @param permNode The permission node to use
     */
    public void registerCommand(ICommand cmd, String permNode) {
        boolean defaultPerm = false;

        if (CmdBase.class.isInstance(cmd)) {
            defaultPerm = ((CmdBase) cmd).canUseWithoutPermission();
        }

        registerCommand(cmd, permNode, defaultPerm);
    }

    /**
     * Registers an ICommand.
     * If the command is a CmdBase, permission is retrieved. Otherwise, the permission is the classpath.
     *
     * @param cmd The ICommand instance to register
     */
    public void registerCommand(ICommand cmd) {
        String permNode = cmd.getClass().getName();

        if (CmdBase.class.isInstance(cmd)) {
            permNode = ((CmdBase) cmd).getPermissionNode();
        }

        registerCommand(cmd, permNode, false);
    }
}
