package mytown.core.utils.economy;

import cpw.mods.fml.common.Loader;
import mytown.core.MyEssentialsCore;
import mytown.core.bukkit.BukkitCompat;
import mytown.core.utils.ItemUtils;
import mytown.core.utils.PlayerUtils;
import mytown.core.utils.economy.forgeessentials.ForgeessentialsEconomy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.reflect.runtime.ThreadLocalStorage;

import java.util.UUID;

/**
 * @author Joe Goett
 */
public class Economy {
    private String costItemName;
    public Class<IEconManager> econManagerClass;

    public Economy(String costItemName) {
        this.costItemName = costItemName;
        if(costItemName.equals("$Vault")) {
            if (MinecraftServer.getServer().getServerModName().contains("cauldron") || MinecraftServer.getServer().getServerModName().contains("mcpc")) {
                econManagerClass = BukkitCompat.initEconomy();
            }
            if(econManagerClass == null)
                throw new RuntimeException("Failed to initialize Vault economy!");
        } else if(costItemName.equals("$ForgeEssentials")) {
            if(Loader.isModLoaded("ForgeEssentials"))
                econManagerClass = (Class<IEconManager>) ((Class<?>) ForgeessentialsEconomy.class);
            if(econManagerClass == null)
                throw new RuntimeException("Failed to initialize ForgeEssentials economy!");
        }
    }

    public IEconManager economyManagerForUUID(UUID uuid) {
        if (econManagerClass == null) {
            return null;
        }
        try {
            IEconManager manager = econManagerClass.newInstance();
            manager.setUUID(uuid);
            return manager;
        } catch(Exception ex) {
            MyEssentialsCore.Instance.log.info("Failed to create IEconManager", ex);
        }

        return null; // Hopefully this doesn't break things...
    }

    /**
     * Takes the amount of money specified.
     * Returns false if player doesn't have the money necessary
     */
    public boolean takeMoneyFromPlayer(EntityPlayer player, int amount) {
        if(costItemName.equals("$ForgeEssentials") || costItemName.equals("$Vault")) {
            IEconManager eco = economyManagerForUUID(player.getUniqueID());
            if (eco == null) return false;
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
        if (costItemName.equals("$ForgeEssentials") || costItemName.equals("$Vault")) {
            IEconManager eco = economyManagerForUUID(player.getUniqueID());
            if (eco == null) return;
            eco.addToWallet(amount);
        } else {
            PlayerUtils.giveItemToPlayer(player, costItemName, amount);
        }
    }

    /**
     * Gets the currency string currently used.
     */
    public String getCurrency(int amount) {
        if(costItemName.equals("$ForgeEssentials") || costItemName.equals("$Vault")) {
            if (econManagerClass == null) {
                return null;
            }
            try {
                IEconManager manager = econManagerClass.newInstance();
                return manager.currency(amount);
            } catch(Exception ex) {
                MyEssentialsCore.Instance.log.info("Failed to create IEconManager", ex);
            }
            return "$";

        } else {
            return ItemUtils.itemStackFromName(costItemName).getDisplayName() + (amount == 1 ? "" : "s");
        }
    }
}
