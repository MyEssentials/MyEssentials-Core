package myessentials.chat.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

/**
 * Helper to build JSON IChatComponents
 */
public class JsonMessageBuilder {
    private JsonMessageBuilder parentBuilder = null;
    private JsonObject rootObj;

    public JsonMessageBuilder() {
        rootObj = new JsonObject();
        rootObj.addProperty("text", "");
    }

    private JsonMessageBuilder(JsonMessageBuilder parentBuilder) {
        this();
        this.parentBuilder = parentBuilder;
    }

    /**
     * Sets the text
     */
    public JsonMessageBuilder setText(String text) {
        rootObj.addProperty("text", text);
        return this;
    }

    /**
     * Adds a new entry in the "extra" list
     */
    public JsonMessageBuilder addExtra() {
        JsonMessageBuilder extra = new JsonMessageBuilder(this);
        if (!rootObj.has("extra")) {
            JsonArray extraArray = new JsonArray();
            rootObj.add("extra", extraArray);
        }
        rootObj.get("extra").getAsJsonArray().add(extra.rootObj);
        return extra;
    }

    /**
     * Sets the color of the text
     */
    public JsonMessageBuilder setColor(EnumChatFormatting color) {
        rootObj.addProperty("color", color.getFriendlyName());
        return this;
    }

    /**
     * Sets if the text is bold
     */
    public JsonMessageBuilder setBold(boolean isBold) {
        rootObj.addProperty("bold", isBold);
        return this;
    }

    /**
     * Sets if the text is underlined
     */
    public JsonMessageBuilder setUnderlined(boolean isUnderlined) {
        rootObj.addProperty("underlined", isUnderlined);
        return this;
    }

    /**
     * Sets if the text is italic
     */
    public JsonMessageBuilder setItalic(boolean isItalic) {
        rootObj.addProperty("italic", isItalic);
        return this;
    }

    /**
     * Sets if the text is strikethrough
     */
    public JsonMessageBuilder setStrikethrough(boolean isStrikethrough) {
        rootObj.addProperty("strikethrough", isStrikethrough);
        return this;
    }

    /**
     * Sets if the text is "obfuscated"
     */
    public JsonMessageBuilder setObfuscated(boolean isObfuscated) {
        rootObj.addProperty("obfuscated", isObfuscated);
        return this;
    }

    /**
     * Sets the text to insert when shift-clicked by a player
     */
    public JsonMessageBuilder setInsertion(String insertion) {
        rootObj.addProperty("insertion", insertion);
        return this;
    }

    /**
     * Sets the translation identifier
     */
    public JsonMessageBuilder setTranslate(String translate) {
        rootObj.addProperty("translate", translate);
        return this;
    }

    /**
     * List of String arguments passed to the translation identifier
     */
    public JsonMessageBuilder setWith(String[] with) {
        JsonArray withArray = new JsonArray();
        for (String str : with) {
            withArray.add(new JsonPrimitive(str));
        }
        rootObj.add("with", withArray);
        return this;
    }

    /**
     * Sets the score
     */
    public JsonMessageBuilder setScore(String name, String objective, String value) {
        JsonObject scoreObj = new JsonObject();
        scoreObj.addProperty("name", name);
        scoreObj.addProperty("objective", objective);
        scoreObj.addProperty("value", value);
        rootObj.add("score", scoreObj);
        return this;
    }

    /**
     * Sets the selector
     */
    public JsonMessageBuilder setSelector(String selector) {
        rootObj.addProperty("selector", selector);
        return this;
    }

    /**
     * Sets the clickEvent to the given action with the value
     * Possible actions are open_url, run_command, and suggest_command
     */
    public JsonMessageBuilder setClickEvent(String action, String value) {
        JsonObject clickEventObj = new JsonObject();
        clickEventObj.addProperty("action", action);
        clickEventObj.addProperty("value", value);
        rootObj.add("clickEvent", clickEventObj);
        return this;
    }

    /**
     * Shortcut to setClickEvent("open_url", url);
     */
    public JsonMessageBuilder setClickEventOpenUrl(String url) {
        return setClickEvent("open_url", url);
    }

    /**
     * Shortcut to setClickEvent("run_command", command);
     */
    public JsonMessageBuilder setClickEventRunCommand(String command) {
        return setClickEvent("run_command", command);
    }

    /**
     * Shortcut to setClickEvent("suggest_command", command);
     */
    public JsonMessageBuilder setClickEventSuggestCommand(String command) {
        return setClickEvent("suggest_command", command);
    }

    /**
     * Sets the hoverEvent to the given action with the value
     * Possible actions are show_text, show_item, show_achievement, and show_entity
     */
    public JsonMessageBuilder setHoverEvent(String action, String value) {
        JsonObject hoverEventObj = new JsonObject();
        hoverEventObj.addProperty("action", action);
        hoverEventObj.addProperty("value", value);
        rootObj.add("hoverEvent", hoverEventObj);
        return this;
    }

    /**
     * Shortcut to setHoverEvent("show_text", text);
     */
    public JsonMessageBuilder setHoverEventShowText(String text) {
        return setHoverEvent("show_text", text);
    }

    /**
     * Shortcut to setHoverEvent("show_item", item);
     */
    public JsonMessageBuilder setHoverEventShowItem(String item) {
        return setHoverEvent("show_item", item);
    }

    /**
     * Shortcut to setHoverEvent("show_achievement", achievement);
     */
    public JsonMessageBuilder setHoverEventShowAchievement(String achievement) {
        return setHoverEvent("show_achievement", achievement);
    }

    /**
     * Shortcut to setHoverEvent("show_entity", entity);
     */
    public JsonMessageBuilder setHoverEventShowEntity(String entity) {
        return setHoverEvent("show_entity", entity);
    }

    public JsonMessageBuilder resetHoverEvent() {
        rootObj.remove("hoverEvent");
        return this;
    }

    /**
     * Returns the parent JsonMessageBuilder, or null if there is no parent
     */
    public JsonMessageBuilder getParent() {
        return parentBuilder;
    }

    /**
     * Returns the root JsonObject
     */
    public JsonObject getRootObj() {
        return rootObj;
    }

    /**
     * Returns the IChatComponent from this builder
     */
    public IChatComponent build() {
        if (parentBuilder == null) {
            return IChatComponent.Serializer.func_150699_a(rootObj.toString());
        } else {
            return parentBuilder.build();
        }
    }
}