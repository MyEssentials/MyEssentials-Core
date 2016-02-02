package myessentials.entities.api.tool;

import myessentials.entities.api.BlockPos;
import myessentials.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;

/**
 * A wrapper class for an instance of an itemstack which executes only on the server-side.
 */
public abstract class Tool {

    /**
     * Every tool starts with this string. Allows easy checks for invalid tools.
     */
    public static final String IDENTIFIER = EnumChatFormatting.BLUE.toString();

    protected EntityPlayer owner;

    /**
     * This is used as an identifier to find the itemstack in the player's inventory.
     */
    protected String toolName;

    protected Tool(EntityPlayer owner, String toolName) {
        this.owner = owner;
        this.toolName = IDENTIFIER + toolName;
    }

    public abstract void onItemUse(BlockPos bp, int face);

    protected abstract String[] getDescription();

    public void onShiftRightClick() {
    }

    public ItemStack getItemStack() {
        return PlayerUtils.getItemStackFromPlayer(owner, Items.wooden_hoe, toolName);
    }

    public void giveItemStack() {
        ItemStack itemStack = new ItemStack(Items.wooden_hoe);
        itemStack.setStackDisplayName(toolName);
        NBTTagList lore = new NBTTagList();
        for(String s : getDescription()) {
            lore.appendTag(new NBTTagString(s));
        }
        itemStack.getTagCompound().getCompoundTag("display").setTag("Lore", lore);
        PlayerUtils.giveItemStackToPlayer(owner, itemStack);
        //owner.sendMessage(MyTown.instance.LOCAL.getLocalization("mytown.notification.tool.gained"));
    }

    protected void updateDescription() {
        NBTTagList lore = getItemStack().getTagCompound().getCompoundTag("display").getTagList("Lore", 8);
        NBTTagList newLore = new NBTTagList();
        String[] newDescription = getDescription();
        for(int i = 0; i < lore.tagCount(); i++) {
            newLore.appendTag(new NBTTagString(newDescription[i]));
        }
        getItemStack().getTagCompound().getCompoundTag("display").setTag("Lore", newLore);
    }
}
