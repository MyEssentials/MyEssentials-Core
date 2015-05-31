package mytown.core.bukkit;

import mytown.core.utils.economy.IEconManager;
import mytown.core.utils.economy.vault.VaultEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Created by AfterWind on 4/6/2015.
 * Everything related to Bukkit, putting this in a separate class since it will crash if server doesn't have bukkit.
 */
public class BukkitCompat {

    public static Class<IEconManager> initEconomy() {
        Server server = Bukkit.getServer();

        RegisteredServiceProvider<Economy> economyProvider = server.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            VaultEconomy.econ = economyProvider.getProvider();
            if (VaultEconomy.econ != null && VaultEconomy.econ.isEnabled()) {
                return (Class<IEconManager>) ((Class<?>) VaultEconomy.class);
                //MyTown.instance.log.info("Enabling Vault economy system!");
            }
        }
        return null;
    }

}
