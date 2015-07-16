package myessentials.test.command;

import myessentials.command.Command;
import myessentials.command.CommandNode;
import net.minecraft.command.ICommandSender;

import java.util.List;

/**
 *
 */
public class TestCommands {

    public static boolean testPermBreach(String permission, ICommandSender sender) {
        return true;
    }

    @Command(
            name="test",
            permission="myessentials.test")
    public static void testCommand(ICommandSender sender, List<String> args) {

    }

    @CommandNode(
            name="sub",
            permission="myessentials.test.sub",
            parentName = "myessentials.test")
    public static void testSubCommand(ICommandSender server, List<String> args) {

    }

}
