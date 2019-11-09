package es.projectalpha.pvp.utils;

import es.projectalpha.pvp.PvpManager;
import org.bukkit.entity.Player;

public class VaultUtils {

    private double getReward(Player p){
        return (PvpManager.getInstance().getVault().getBalance(p) * 0.1);
    }

    public void killMoney(Player p, Player pl){
        PvpManager.getInstance().getVault().depositPlayer(p, getReward(pl));
        PvpManager.getInstance().getVault().withdrawPlayer(pl, getReward(pl));
    }
}
