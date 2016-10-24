package myessentials.entities.api.new_sign;

import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;

public class ImmutableShopSignData extends AbstractImmutableData<ImmutableShopSignData, ShopSignData> {

    private String ownerUUID;
    private int price;

    public ImmutableShopSignData(String ownerUUID, int price) {
        this.ownerUUID = ownerUUID;
        this.price = price;
    }

    @Override
    protected void registerGetters() {

    }

    @Override
    public ShopSignData asMutable() {
        return new ShopSignData(ownerUUID, price);
    }

    @Override
    public int compareTo(ImmutableShopSignData o) {
        return 0;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
