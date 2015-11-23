package myessentials.entities.sign;

import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntitySign;

public abstract class SignType {
    public abstract String getTypeID();

    public abstract Sign loadData(TileEntitySign tileEntity, NBTBase signData);

    public void register() {
        SignManager.instance.signTypes.put(getTypeID(), this);
    }
}
