package myessentials.test.chat;

import myessentials.chat.api.IChatFormat;
import myessentials.chat.api.TextComponentFormatted;
import org.junit.Assert;
import myessentials.exception.FormatException;
import myessentials.test.MECTest;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.junit.Test;

public class ChatComponentFormattedTest extends MECTest {

    private class MockArgument implements IChatFormat {

        private int x;
        public MockArgument(int x) {
            this.x = x;
        }

        @Override
        public IChatComponent toChatMessage() {
            return new ChatComponentText(x + "");
        }
    }

    @Test
    public void shouldCreateEmptyMessage() {

        TextComponentFormatted message = new TextComponentFormatted("");
        Assert.assertEquals("Message should've been empty", "", message.getUnformattedText());

    }

    @Test
    public void shouldCreateMessageFromArguments() {

        TextComponentFormatted message = new TextComponentFormatted("{%s}{%s}", new MockArgument(5), new MockArgument(20));
        Assert.assertEquals("Message did not get created properly", "520", message.getUnformattedText());

    }

    @Test
    public void shouldCreateMessageFromStringArguments() {

        TextComponentFormatted message = new TextComponentFormatted("{|%s}{|%s}", 5 + "", 20 + "");
        Assert.assertEquals("Message did not get created properly", "520", message.getUnformattedText());

    }

    @Test
    public void shouldCreateMessageFromStringWithProperStyle() {

        TextComponentFormatted message = new TextComponentFormatted("{1|%s}{2l|%s}", 5 + "", 20 + "");
        Assert.assertEquals("Message did not get created properly", "520", message.getUnformattedText());
        Assert.assertEquals("Color was not the correct one for the first argument", EnumChatFormatting.DARK_BLUE, ((IChatComponent) message.getSiblings().get(0)).getChatStyle().getColor());
        Assert.assertEquals("Color was not the correct one for the second argument", EnumChatFormatting.DARK_GREEN, ((IChatComponent) message.getSiblings().get(1)).getChatStyle().getColor());
        Assert.assertTrue("Second argument was not formatted bold", ((IChatComponent) message.getSiblings().get(1)).getChatStyle().getBold());

    }

    @Test(expected = FormatException.class)
    public void shouldFailToCreateMessage() {

        TextComponentFormatted message = new TextComponentFormatted("%s%s", new MockArgument(5), new MockArgument(10));
        Assert.assertEquals("Message did not get created properly", "510", message.getUnformattedText());

    }

    @Test
    public void shouldCreateMessageWithComponentArguments() {

        TextComponentFormatted message = new TextComponentFormatted("{%s}{%s}", new MockArgument(120), new ChatComponentText("420"));
        Assert.assertEquals("Message did not get created properly", "120420", message.getUnformattedText());

    }

}
