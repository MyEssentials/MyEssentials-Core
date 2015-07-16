package myessentials.test.command;

import myessentials.command.Command;
import myessentials.command.CommandManager;
import net.minecraft.command.ICommandSender;
import org.junit.Test;

import java.util.List;

/**
 * Test the command system
 */
public class CommandTest {

    @Test
    public void testRegister() throws Exception {
        CommandManager.registerCommands(this, getClass().getMethod("testPermBreach"));
    }

    @Test
    public void testCommandCall() {

        //CommandManager.commandCall();
    }

    public boolean testPermBreach() {
        return true;
    }

    @Command(
            name="test",
            permission="myessentials.test")
    public static void testCommand(ICommandSender sender, List<String> args) {

    }



}
