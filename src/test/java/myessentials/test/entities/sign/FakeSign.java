package myessentials.test.entities.sign;

import myessentials.entities.api.sign.Sign;
import myessentials.entities.api.sign.SignType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;

public class FakeSign extends Sign {

    public int amountOfClicks = 0;

    protected FakeSign(int amountOfClicks) {
        super(FakeSignType.instance);
        this.amountOfClicks = amountOfClicks;
    }

    @Override
    public void onRightClick(EntityPlayer player) {
        this.amountOfClicks++;
    }

    @Override
    public void onShiftRightClick(EntityPlayer player) {
        this.amountOfClicks += 2;
    }

    @Override
    protected String[] getText() {
        return new String[] {
            "",
            "",
            "",
            amountOfClicks + ""
        };
    }

    // REF: Having to create your own SignType does not seem desirable
    public static class FakeSignType extends SignType {
        public static final FakeSignType instance = new FakeSignType();

        @Override
        public String getTypeID() {
            return "MEC:FakeSign";
        }

        @Override
        public Sign loadData(TileEntitySign tileEntity, NBTBase signData) {
            return new FakeSign(((NBTTagCompound) signData).getInteger("someData"));
        }

        @Override
        public boolean isTileValid(TileEntitySign te) {
            return true;
        }
    }
}
