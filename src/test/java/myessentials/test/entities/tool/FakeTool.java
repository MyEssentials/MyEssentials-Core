package myessentials.test.entities.tool;

import myessentials.entities.api.BlockPos;
import myessentials.entities.api.tool.Tool;
import net.minecraft.entity.player.EntityPlayer;

public class FakeTool extends Tool {

    public int amountOfClicks = 0;

    protected FakeTool(EntityPlayer owner) {
        super(owner, "FakestTool");
    }

    // REF: Change the name of method to something different since it can be confused with the onItemUse method in items
    @Override
    public void onItemUse(BlockPos bp, int face) {
        amountOfClicks++;
    }

    @Override
    public void onShiftRightClick() {
        amountOfClicks += 2;
    }

    @Override
    protected String[] getDescription() {
        return new String[] {
                "",
                amountOfClicks + "",
                (amountOfClicks + 1) + "",
                ""
        };
    }
}
