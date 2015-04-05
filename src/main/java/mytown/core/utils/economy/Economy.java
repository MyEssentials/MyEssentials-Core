package mytown.core.utils.economy;

import mytown.core.utils.economy.forgeessentials.ForgeessentialsEconomy;
import mytown.core.utils.economy.vault.VaultEconomy;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

/**
 * @author Joe Goett
 */
public class Economy {
    public Class<IEconManager> econManagerClass;

    public Economy(String costItemName) {
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
            //MyTown.instance.log.info("Failed to create IEconManager", ex);
        }

        return null; // Hopefully this doesn't break things...
    }
}
