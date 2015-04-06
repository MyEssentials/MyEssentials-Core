package mytown.core.utils.economy;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import mytown.core.utils.ItemUtils;
import mytown.core.utils.PlayerUtils;
import mytown.core.utils.economy.forgeessentials.ForgeessentialsEconomy;
import mytown.core.utils.economy.vault.VaultEconomy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

/**
 * @author Joe Goett
 */
public class Economy {
    private static Logger log = LogManager.getLogger("MyEconomy");
    private String costItemName;
    public Class<IEconManager> econManagerClass;

    public Economy(String costItemName) {
        this.costItemName = costItemName;
        if(costItemName.equals("$Vault")) {
            if (MinecraftServer.getServer().getServerModName().contains("cauldron") || MinecraftServer.getServer().getServerModName().contains("mcpc")) {
                Server server = Bukkit.getServer();

                RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider = server.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
                if (economyProvider != null) {
                    VaultEconomy.econ = economyProvider.getProvider();
                    if (VaultEconomy.econ != null && VaultEconomy.econ.isEnabled()) {
                        econManagerClass = (Class<IEconManager>) ((Class<?>) VaultEconomy.class);
                        //MyTown.instance.log.info("Enabling Vault economy system!");
                    }
                }
            }
        } else if(costItemName.equals("$ForgeEssentials")) {
            econManagerClass = (Class<IEconManager>) ((Class<?>) ForgeessentialsEconomy.class);
            //MyTown.instance.log.info("Enabling ForgeEssentials economy system!");
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
            log.info("Failed to create IEconManager", ex);
        }

        return null; // Hopefully this doesn't break things...
    }

    /**
     * Takes the amount of money specified.
     * Returns false if player doesn't have the money necessary
     */
    public boolean takeMoneyFromPlayer(EntityPlayer player, int amount) {
        if(costItemName.startsWith("$")) {
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
        if (costItemName.startsWith("$")) {
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
                log.info("Failed to create IEconManager", ex);
            }
            return "$";

        } else {
            return ItemUtils.itemStackFromName(costItemName).getDisplayName() + (amount == 1 ? "" : "s");
        }
    }

    /**
     * Returns true if the string matches one of the implemented Economy systems.
     * Returns false if the item based Economy is used.
     */
    public boolean checkCurrencyString(String currencyString) {
        if(currencyString.equals("$ForgeEssentials")) {
            if(!Loader.isModLoaded("ForgeEssentials") || econManagerClass == null)
                throw new RuntimeException("ForgeEssentials economy failed to initialize.");
            return true;
        } else if(currencyString.equals("$Vault")) {
            if(econManagerClass == null)
                throw new RuntimeException("Vault economy failed to initialize");
            return true;
        }
        return false;
    }
}
