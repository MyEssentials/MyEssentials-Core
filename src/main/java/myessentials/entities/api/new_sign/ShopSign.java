package myessentials.entities.api.new_sign;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public abstract class ShopSign {

    protected Player owner;
    protected Location<World> location;
    protected int price;

    public ShopSign(Player owner, Location<World> location, int price) {
        this.owner = owner;
        this.location = location;
        this.price = price;
    }

    public abstract void onBuy();

}
