package mytown.core.economy;

import cpw.mods.fml.common.Loader;
import mytown.core.MyEssentialsCore;
import mytown.core.bukkit.BukkitCompat;
import mytown.core.exception.EconomyException;
import mytown.core.utils.ItemUtils;
import mytown.core.utils.PlayerUtils;
import mytown.core.economy.forgeessentials.ForgeessentialsEconomy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public class Economy {
    public static final String CURRENCY_VAULT = "$Vault";
    public static final String CURRENCY_FORGE_ESSENTIALS = "$ForgeEssentials";

    private String costItemName;
    private Class<? extends IEconManager> econManagerClass;

    public Economy(String costItemName) {
        this.costItemName = costItemName;
        if(costItemName.equals(CURRENCY_VAULT)) {
            if (MinecraftServer.getServer().getServerModName().contains("cauldron") || MinecraftServer.getServer().getServerModName().contains("mcpc")) {
                econManagerClass = BukkitCompat.initEconomy();
            }
            if(econManagerClass == null)
                throw new EconomyException("Failed to initialize Vault economy!");
        } else if(costItemName.equals(CURRENCY_FORGE_ESSENTIALS)) {
            if(Loader.isModLoaded("ForgeEssentials"))
                econManagerClass = ForgeessentialsEconomy.class;
            if(econManagerClass == null)
                throw new EconomyException("Failed to initialize ForgeEssentials economy!");
        }
    }

    public IEconManager economyManagerForUUID(UUID uuid) {
        if (econManagerClass == null) {
            return null;
        }
        try {
            IEconManager manager = econManagerClass.newInstance();
            manager.setPlayer(uuid);
            return manager;
        } catch(Exception ex) {
            MyEssentialsCore.instance.LOG.info("Failed to create IEconManager", ex);
        }

        return null; // Hopefully this doesn't break things...
    }

    /**
     * Takes the amount of money specified.
     * Returns false if player doesn't have the money necessary
     */
    public boolean takeMoneyFromPlayer(EntityPlayer player, int amount) {
        if(costItemName.equals(CURRENCY_FORGE_ESSENTIALS) || costItemName.equals(CURRENCY_VAULT)) {
            IEconManager eco = economyManagerForUUID(player.getUniqueID());
            if (eco == null)
                return false;
            int wallet = eco.getWallet();
            if (wallet >= amount) {
                eco.removeFromWallet(amount);
                return true;
            }
            return false;
        } else {
            return PlayerUtils.takeItemFromPlayer(player, costItemName, amount);
        }
    }

    /**
     * Takes the amount of money specified.
     * Returns false if player doesn't have the money necessary
     */
    public void giveMoneyToPlayer(EntityPlayer player, int amount) {
        if (costItemName.equals(CURRENCY_FORGE_ESSENTIALS) || costItemName.equals(CURRENCY_VAULT)) {
            IEconManager eco = economyManagerForUUID(player.getUniqueID());
            if (eco == null)
                return;
            eco.addToWallet(amount);
        } else {
            PlayerUtils.giveItemToPlayer(player, costItemName, amount);
        }
    }

    /**
     * Gets the currency string currently used.
     */
    public String getCurrency(int amount) {
        if(costItemName.equals(CURRENCY_FORGE_ESSENTIALS) || costItemName.equals(CURRENCY_VAULT)) {
            if (econManagerClass == null) {
                return null;
            }
            try {
                IEconManager manager = econManagerClass.newInstance();
                return manager.currency(amount);
            } catch(Exception ex) {
                MyEssentialsCore.instance.LOG.info("Failed to create IEconManager", ex);
            }
            return "$";

        } else {
            return ItemUtils.itemStackFromName(costItemName).getDisplayName() + (amount == 1 ? "" : "s");
        }
    }
}
