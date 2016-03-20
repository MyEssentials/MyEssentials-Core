package myessentials.test.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import junit.framework.Assert;
import myessentials.chat.api.JsonMessageBuilder;
import myessentials.test.MECTest;
import net.minecraft.util.EnumChatFormatting;
import org.junit.Test;

public class JsonMessageBuilderTest extends MECTest {

    @Test
    public void shouldCreateEmptyMessage() {
        JsonMessageBuilder builder = new JsonMessageBuilder();
        Assert.assertEquals("Failed to create an empty message", "", builder.getRootObj().get("text").getAsString());
    }

    @Test
    public void shouldSetTextWithFormatting() {
        JsonMessageBuilder builder = new JsonMessageBuilder().setText("test123");
        Assert.assertEquals("Failed to set proper text when building a json message", "test123", builder.getRootObj().get("text").getAsString());

        builder.setBold(true);
        Assert.assertTrue("Failed to set text format to bold", builder.getRootObj().get("bold").getAsBoolean());

        builder.setColor(EnumChatFormatting.BLUE);
        Assert.assertEquals("Failed to set text to color blue", EnumChatFormatting.BLUE.getFriendlyName(), builder.getRootObj().get("color").getAsString());

        builder.setUnderlined(true);
        Assert.assertTrue("Failed to set text format to underlined", builder.getRootObj().get("underlined").getAsBoolean());

        builder.setItalic(true);
        Assert.assertTrue("Failed to set text format to italic", builder.getRootObj().get("italic").getAsBoolean());

        builder.setStrikethrough(true);
        Assert.assertTrue("Failed to set text format to strikethrough", builder.getRootObj().get("strikethrough").getAsBoolean());

        builder.setObfuscated(true);
        Assert.assertTrue("Failed to set text format to italic", builder.getRootObj().get("obfuscated").getAsBoolean());
    }

    @Test
    public void shouldSetInsertionText() {
        JsonMessageBuilder builder = new JsonMessageBuilder();

        builder.setInsertion("insertion123");
        Assert.assertEquals("Failed to set insertion text", "insertion123", builder.getRootObj().get("insertion").getAsString());
    }

    @Test
    public void shouldSetClickEventProperties() {
        JsonMessageBuilder builder = new JsonMessageBuilder();

        builder.setClickEvent("invalid_action", "Some text");
        Assert.assertFalse("An invalid click action should not be added to the json builder", builder.getRootObj().has("clickEvent"));

        // REF: Make event actions an enum so that when the API changes we don't have to change the tests
        // REF: OR simply remove the generic setClickEvent method (less desirable)
        builder.setClickEventOpenUrl("some valid url");
        Assert.assertEquals("Event action did not get set properly", "open_url", builder.getRootObj().get("clickEvent").getAsJsonObject().get("action").getAsString());
        Assert.assertEquals("Url did not get set properly", "some valid url", builder.getRootObj().get("clickEvent").getAsJsonObject().get("value").getAsString());

        builder.setClickEventRunCommand("some valid command");
        Assert.assertEquals("Event action did not get set properly", "run_command", builder.getRootObj().get("clickEvent").getAsJsonObject().get("action").getAsString());
        Assert.assertEquals("Url did not get set properly", "some valid command", builder.getRootObj().get("clickEvent").getAsJsonObject().get("value").getAsString());

        builder.setClickEventSuggestCommand("some valid command");
        Assert.assertEquals("Event action did not get set properly", "suggest_command", builder.getRootObj().get("clickEvent").getAsJsonObject().get("action").getAsString());
        Assert.assertEquals("Url did not get set properly", "some valid command", builder.getRootObj().get("clickEvent").getAsJsonObject().get("value").getAsString());

        // REF: There is a reset hover event method but not a reset click event method.
    }

    @Test
    public void shouldSetHoverEventProperties() {
        JsonMessageBuilder builder = new JsonMessageBuilder();

        builder.setHoverEvent("invalid_action", "Something");
        Assert.assertFalse("An invalid action should not be added to the json builder", builder.getRootObj().has("hoverEvent"));

        builder.setHoverEventShowAchievement("some achievement");
        Assert.assertEquals("Event action did not get set properly", "show_achievement", builder.getRootObj().get("hoverEvent").getAsJsonObject().get("action").getAsString());
        Assert.assertEquals("Achievement string did not get set properly", "some achievement", builder.getRootObj().get("hoverEvent").getAsJsonObject().get("value").getAsString());

        builder.setHoverEventShowEntity("some entity");
        Assert.assertEquals("Event action did not get set properly", "show_entity", builder.getRootObj().get("hoverEvent").getAsJsonObject().get("action").getAsString());
        Assert.assertEquals("Achievement string did not get set properly", "some entity", builder.getRootObj().get("hoverEvent").getAsJsonObject().get("value").getAsString());

        builder.setHoverEventShowItem("some item");
        Assert.assertEquals("Event action did not get set properly", "show_item", builder.getRootObj().get("hoverEvent").getAsJsonObject().get("action").getAsString());
        Assert.assertEquals("Achievement string did not get set properly", "some item", builder.getRootObj().get("hoverEvent").getAsJsonObject().get("value").getAsString());

        builder.setHoverEventShowText("some text");
        Assert.assertEquals("Event action did not get set properly", "show_text", builder.getRootObj().get("hoverEvent").getAsJsonObject().get("action").getAsString());
        Assert.assertEquals("Achievement string did not get set properly", "some text", builder.getRootObj().get("hoverEvent").getAsJsonObject().get("value").getAsString());

        builder.resetHoverEvent();
        Assert.assertFalse("Hover event property should have been deleted", builder.getRootObj().has("hoverEvent"));
    }

    @Test
    public void shouldSetTranslation() {
        JsonMessageBuilder builder = new JsonMessageBuilder();

        builder.setTranslate("translation123");
        Assert.assertEquals("Failed to set insertion text", "translation123", builder.getRootObj().get("translate").getAsString());

        String[] args = new String[]{
                "arg1",
                "arg2",
                "arg3",
                "lastArg"
        };
        builder.setWith(args);
        JsonArray array = builder.getRootObj().get("with").getAsJsonArray();

        int i = 0;
        for (JsonElement json : array) {
            Assert.assertEquals("An argument did not get added to the translation table", args[i++], json.getAsString());
        }
    }
}
