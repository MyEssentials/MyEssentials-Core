package myessentials.economy.core.vault;

import myessentials.economy.api.IEconManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Everything related to Bukkit, putting this in a separate class since it will crash if server doesn't have bukkit.
 */
public class BukkitCompat {

    private BukkitCompat() {

    }

    public static Class<? extends IEconManager> initEconomy() {
        Server server = Bukkit.getServer();

        RegisteredServiceProvider<Economy> economyProvider = server.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            VaultEconomy.econ = economyProvider.getProvider();
            if (VaultEconomy.econ != null && VaultEconomy.econ.isEnabled()) {
                return VaultEconomy.class;
                //return (Class<IEconManager>) ((Class<?>) VaultEconomy.class);
            }
        }
        return null;
    }

}
