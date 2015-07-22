package myessentials.test.command;

import myessentials.command.annotation.CommandNode;
import net.minecraft.command.ICommandSender;

import java.util.List;

/**
 *
 */
public class TestCommands {

    public static boolean testPermBreach(String permission, ICommandSender sender) {
        return true;
    }

    @CommandNode(
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
