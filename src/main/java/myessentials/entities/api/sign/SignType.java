package myessentials.entities.api.sign;

import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntitySign;

/**
 * A type of a wrapped sign block on the server side.
 */
public abstract class SignType {
    /**
     * The unique ID for this type.
     * @return The ID following the syntax: "modId:signType"
     */
    public abstract String getTypeID();

    /**
     * Loads a sign data of this type.
     * @param tileEntity The tile entity that is being loaded
     * @param signData The data stored on the sign
     * @return The loaded sign
     */
    public abstract Sign loadData(TileEntitySign tileEntity, NBTBase signData);

    /**
     * Register this type on the {@link SignManager}, all types must be registered to be recognized.
     */
    public void register() {
        SignManager.instance.signTypes.put(getTypeID(), this);
    }

    public abstract boolean isTileValid(TileEntitySign te);
}
