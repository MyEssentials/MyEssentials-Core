package myessentials.entities.api.new_sign;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class ShopSignData extends AbstractData<ShopSignData, ImmutableShopSignData> {

    public static final Key<Value<Integer>> PRICE_KEY = KeyFactory.makeSingleKey(Integer.TYPE, Value.class, DataQuery.of("MyTown2", "sign", "price"));
    public static final Key<Value<String>> OWNER_KEY = KeyFactory.makeSingleKey(String.class, Value.class, DataQuery.of("MyTown2", "sign", "owner"));

    private String ownerUUID;
    private int price;

    public ShopSignData(String ownerUUID, int price) {
        this.ownerUUID = ownerUUID;
        this.price = price;
    }

    @Override
    protected void registerGettersAndSetters() {
        // wut
    }

    @Override
    public Optional<ShopSignData> fill(DataHolder dataHolder, MergeFunction overlap) {

        Optional<Integer> price = dataHolder.get(PRICE_KEY);
        Optional<String> owner = dataHolder.get(OWNER_KEY);

        if (owner.isPresent()) {
            this.ownerUUID = owner.get();
        }

        if (price.isPresent()) {
            this.price = price.get();
        }

        return (owner.isPresent() && price.isPresent()) ? Optional.of(this) : Optional.empty();
    }

    @Override
    public Optional<ShopSignData> from(DataContainer container) {
        Optional<Object> ownerUUID = container.get(DataQuery.of("MyTown2", "sign", "owner"));
        Optional<Object> price = container.get(DataQuery.of("MyTown2", "sign", "price"));

        if (ownerUUID.isPresent() && ownerUUID.get() instanceof String) {
            this.ownerUUID = (String) ownerUUID.get();
        }

        if (price.isPresent() && price.get() instanceof Integer) {
            this.price = (int) price.get();
        }

        return (ownerUUID.isPresent() && price.isPresent()) ? Optional.of(this) : Optional.empty();
    }

    @Override
    public ShopSignData copy() {
        return new ShopSignData(ownerUUID, price);
    }

    @Override
    public ImmutableShopSignData asImmutable() {
        return new ImmutableShopSignData(ownerUUID, price);
    }

    @Override
    public int compareTo(ShopSignData o) {
        return 0;
    }

    @Override
    public int getContentVersion() {
        return -900;
    }
}
