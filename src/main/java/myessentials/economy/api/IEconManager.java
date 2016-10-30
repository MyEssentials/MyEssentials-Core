package myessentials.economy.api;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;
import java.util.UUID;

public interface IEconManager {

    void setPlayer(UUID uuid);

    /**
     * Add a set amount to a target's Wallet
     */
    void addToWallet(int amountToAdd);

    /**
     * Get the amount of money the player has
     */
    int getWallet();

    /**
     * Remove a set amount from a target's Wallet
     * returns true if it succeded, false if it didn't
     */
    boolean removeFromWallet(int amountToSubtract);

    /**
     * Set the target's Wallet to the specified amount
     */
    void setWallet(int setAmount, EntityPlayer player);

    /**
     * Gets the singular or plural term of the currency used
     */
    String currency(int amount);

    /**
     * Gets a combo of getWallet + currency
     */
    String getMoneyString();
    
    /**
     * Saves all wallets to disk
     * (for users still on the server when it's stopping)
     */
    void save();

    Map<String, Integer> getItemTables();
}
