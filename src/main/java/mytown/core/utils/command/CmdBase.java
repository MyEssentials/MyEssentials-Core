package mytown.core.utils.command;

import mytown.core.utils.Assert;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public abstract class CmdBase extends CommandBase {
    public abstract String getPermissionNode();

    public abstract boolean canConsoleUseCommand();

    public abstract boolean canRConUseCommand();

    public abstract boolean canCommandBlockUseCommand();

    public boolean canUseWithoutPermission() {
        return false;
    }


}
