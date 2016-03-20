package myessentials.test.chat;

import junit.framework.Assert;
import myessentials.chat.api.ChatComponentMultiPage;
import myessentials.test.MECTest;
import net.minecraft.util.ChatComponentText;
import org.junit.Test;

public class ChatComponentMultiPageTest extends MECTest {

    @Test
    public void shouldCreateComponentProperly() {

        ChatComponentMultiPage message = new ChatComponentMultiPage(4);
        for (int i = 1; i <= 10; i++) {
            message.add(new ChatComponentText("Some texts: " + i));
        }
        Assert.assertEquals("ChatComponentMultiPage does not have the expected amount of pages", 3, message.getNumberOfPages());
        Assert.assertEquals("ChatComponentMultiPage does not have the expected amount of components in page 1", 4, message.getPage(1).size());
        Assert.assertEquals("ChatComponentMultiPage does not have the expected amount of components in page 2", 4, message.getPage(2).size());
        Assert.assertEquals("ChatComponentMultiPage does not have the expected amount of components in page 3", 2, message.getPage(3).size());

    }

    @Test
    public void shouldCreateEmptyComponent() {

        ChatComponentMultiPage message = new ChatComponentMultiPage(4);
        Assert.assertEquals("ChatComponentMultiPage does not have the expected amount of pages", 0, message.getNumberOfPages());
        Assert.assertEquals("ChatComponentMultiPage does not return an empty list when it's empty", 0, message.getPage(1).size());

    }

    @Test
    public void shouldCreateAlmostEmptyComponenent() {

        ChatComponentMultiPage message = new ChatComponentMultiPage(4);
        message.add(new ChatComponentText("0"));
        Assert.assertEquals("ChatComponentMultiPage does not have the expected amount of pages", 1, message.getNumberOfPages());
        Assert.assertEquals("ChatComponentMultiPage does not return an empty list when it's empty", 1, message.getPage(1).size());

    }
}
