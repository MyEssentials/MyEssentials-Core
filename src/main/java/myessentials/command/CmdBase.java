package myessentials.command;

import net.minecraft.command.CommandBase;


/**
 * The basic command layout for all commands.
 *
 */
public abstract class CmdBase extends CommandBase {

    /**
     * Gets the unique identifier which doubles as a permission node.
     *
     * @return
     */
    public abstract String getPermissionNode();

    public abstract boolean canConsoleUseCommand();

    public abstract boolean canRConUseCommand();

    public abstract boolean canCommandBlockUseCommand();

    /**
     * If the command can be used without any permission checks.
     *
     * @return
     */
    public boolean canUseWithoutPermission() {
        return false;
    }


}
