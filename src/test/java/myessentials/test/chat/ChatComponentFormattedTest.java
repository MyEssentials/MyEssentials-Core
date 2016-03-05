package myessentials.test.chat;

import junit.framework.Assert;
import myessentials.chat.api.ChatComponentFormatted;
import myessentials.chat.api.ChatFormat;
import myessentials.exception.FormatException;
import myessentials.test.MECTest;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.junit.Test;

public class ChatComponentFormattedTest extends MECTest {

    private class MockArgument extends ChatFormat {

        private int x;
        public MockArgument(int x) {
            this.x = x;
        }

        @Override
        public IChatComponent toChatMessage(boolean shortened) {
            return new ChatComponentText(x + "");
        }
    }

    @Test
    public void shouldCreateEmptyMessage() {

        ChatComponentFormatted message = new ChatComponentFormatted("");
        Assert.assertEquals("Message should've been empty", "", message.getUnformattedText());

    }

    @Test
    public void shouldCreateMessageFromArguments() {

        ChatComponentFormatted message = new ChatComponentFormatted("{%s}{%s}", new MockArgument(5), new MockArgument(20));
        Assert.assertEquals("Message did not get created properly", "520", message.getUnformattedText());

    }

    @Test
    public void shouldCreateMessageFromStringArguments() {

        ChatComponentFormatted message = new ChatComponentFormatted("{|%s}{|%s}", 5 + "", 20 + "");
        Assert.assertEquals("Message did not get created properly", "520", message.getUnformattedText());

    }

    @Test
    public void shouldCreateMessageFromStringWithProperStyle() {

        ChatComponentFormatted message = new ChatComponentFormatted("{1|%s}{2l|%s}", 5 + "", 20 + "");
        Assert.assertEquals("Message did not get created properly", "520", message.getUnformattedText());
        Assert.assertEquals("Color was not the correct one for the first argument", EnumChatFormatting.DARK_BLUE, ((IChatComponent) message.getSiblings().get(0)).getChatStyle().getColor());
        Assert.assertEquals("Color was not the correct one for the second argument", EnumChatFormatting.DARK_GREEN, ((IChatComponent) message.getSiblings().get(1)).getChatStyle().getColor());
        Assert.assertTrue("Second argument was not formatted bold", ((IChatComponent) message.getSiblings().get(1)).getChatStyle().getBold());

    }

    @Test(expected = FormatException.class)
    public void shouldFailToCreateMessage() {

        ChatComponentFormatted message = new ChatComponentFormatted("%s%s", new MockArgument(5), new MockArgument(10));
        Assert.assertEquals("Message did not get created properly", "510", message.getUnformattedText());

    }

    @Test
    public void shouldCreateMessageWithComponentArguments() {

        ChatComponentFormatted message = new ChatComponentFormatted("{%s}{%s}", new MockArgument(120), new ChatComponentText("420"));
        Assert.assertEquals("Message did not get created properly", "120420", message.getUnformattedText());

    }

}
