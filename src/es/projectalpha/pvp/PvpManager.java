package es.projectalpha.pvp;

import es.projectalpha.pvp.cmd.PvPCMD;
import es.projectalpha.pvp.files.Message;
import es.projectalpha.pvp.events.EntityEvent;
import es.projectalpha.pvp.events.PlayerEvent;
import es.projectalpha.pvp.files.Files;
import es.projectalpha.pvp.files.Lang;
import es.projectalpha.pvp.manager.Manager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ConcurrentModificationException;
import java.util.HashMap;

public class PvpManager extends JavaPlugin {

	public HashMap<Player, Integer> cooldown = new HashMap<>();
	public HashMap<Player, Integer> pvpCooldown = new HashMap<>();
	public HashMap<Player, Integer> newbieCooldown = new HashMap<>();

	private Files files = new Files();
	private Lang lang = new Lang();

	private Manager manager;

	private Economy vault;

	private static PvpManager instance;

	public void onEnable(){
		instance = this;
		manager = new Manager();

		files.setupFiles();
		lang.setupLang();

		registerEvents();
		registerCommands();

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            vault = economyProvider.getProvider();
        }

        System.out.println("Pvpmanager ha sido cargado");
		startCooldowns();
	}

	public void onDisable(){ System.out.println("Pvpmanager ha sido descargado"); }

	private void registerEvents(){
	    new EntityEvent(this, manager);
	    new PlayerEvent(this, manager);
    }

	private void startCooldowns(){

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run(){
				try{
					cooldown.keySet().forEach(p -> {
						int x = cooldown.get(p);

						x--;
						if(x <= 0){
							cooldown.remove(p);
							return;
						}
						cooldown.put(p, x);
					});
				}catch(ConcurrentModificationException e){}

				try{
					pvpCooldown.keySet().forEach(pl -> {
						int x = pvpCooldown.get(pl);

						x--;
						if(x <= 0){
							pvpCooldown.remove(pl);
							pl.sendMessage(Message.prefix + ChatColor.DARK_GREEN + " Ya no estás en pvp, puedes desconectarte.");
							return;
						}
						pvpCooldown.put(pl, x);
					});
				}catch(ConcurrentModificationException e){}

				try{
					newbieCooldown.keySet().forEach(ple -> {
						int x = newbieCooldown.get(ple);

						x--;
						if(x <= 0){
							newbieCooldown.remove(ple);
							return;
						}
						newbieCooldown.put(ple, x);
					});
				}catch(ConcurrentModificationException e){}
			}
		},1, 20);
	}

	private void registerCommands(){
		getCommand("pvp").setExecutor(new PvPCMD());
	}

	public Economy getVault(){
	    return vault;
    }

	public Manager getManager(){
	    return manager;
    }

	public static PvpManager getInstance(){
		return instance;
	}
}
