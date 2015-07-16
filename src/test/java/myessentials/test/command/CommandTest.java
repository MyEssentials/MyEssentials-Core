package myessentials.test.command;

import myessentials.command.CommandManager;
import myessentials.test.utils.TestCommandSender;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Have this be tested when we have a way to run a full minecraft server during the test
 *
 * Test the command system
 */
public class CommandTest {

    //
    // Test methods
    //

    //@Before
    public void testRegister() throws Exception {
        CommandManager.registerCommands(TestCommands.class);
    }

    //@Test
    public void testCommandCall() {
        List<String> args = new ArrayList<String>();
        args.add("this");
        args.add("is");
        args.add("a");
        args.add("test");
        CommandManager.commandCall("myessentials.test", new TestCommandSender(), args);
    }

    //@Test
    public void testCommandExistance() {
        Assert.assertEquals("test", CommandManager.commandNames.get("myessentials.test"));
        Assert.assertEquals("sub", CommandManager.commandNames.get("myessentials.test.sub"));
        Assert.assertEquals("myessentails.test", CommandManager.commandParents.get("myessentials.test.sub"));
    }
}
