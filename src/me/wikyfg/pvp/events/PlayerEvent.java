package me.wikyfg.pvp.events;

import me.wikyfg.pvp.PvpManager;
import me.wikyfg.pvp.files.Files;
import me.wikyfg.pvp.files.Message;
import me.wikyfg.pvp.manager.Manager;
import me.wikyfg.pvp.utils.VaultUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class PlayerEvent implements Listener {

	private Manager manager;
	private VaultUtils vu = new VaultUtils();
	private PvpManager plugin;
	public PlayerEvent(PvpManager PvpManager, Manager manager){
        this.plugin = PvpManager;
        this.manager = manager;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();

		if(!Files.user.contains("Users." + p.getName())){

			Files.user.set("Users." + p.getName() + ".pvp", false);
			
			manager.addNewbie(p);
			
			try{
				Files.user.save(Files.users);
				Files.user.load(Files.users);
			}catch(IOException | InvalidConfigurationException e){
				e.printStackTrace();
			}

			return;
		}
		
	}

	@EventHandler
    public void onDeath(PlayerDeathEvent e){
	    Entity en1 = e.getEntity();
	    Entity en = e.getEntity().getKiller();
	    
	    if (en instanceof Player){
	        Player pl = (Player)en1;
            Player p = (Player)en;

			if(manager.isInPvP(p)){
                vu.killMoney(p, pl);
            	e.setKeepInventory(true);

				plugin.getManager().removeCooldown(p);
				plugin.getManager().removeCooldown(pl);

            	p.sendMessage(Message.prefix + ChatColor.DARK_GREEN + " Ya no estás en pvp, puedes desconectarte.");
            	pl.sendMessage(Message.prefix + ChatColor.DARK_GREEN + " Ya no estás en pvp, puedes desconectarte.");
            }
            
        }
    }
	@EventHandler
	public void BlockPlaceEvent(org.bukkit.event.block.BlockPlaceEvent e){
		Player p = e.getPlayer();
		Block b = e.getBlock();
		
		for(Entity en : p.getNearbyEntities (4D, 4D, 4D)){
			
			if (en instanceof Player){
				
				if(en == p) continue;
				
				if(b.getType() == Material.FIRE){
							
					if(!Files.user.getBoolean("Users." + en.getName() + ".pvp")){
							p.sendMessage(Message.prefix + ChatColor.DARK_RED + " No puedes poner ese bloque cerca de un jugador con el pvp desactivado.");
							e.setCancelled(true);
					}
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onInteract(PlayerBucketEmptyEvent e){
		Player p = e.getPlayer();
		for(Entity en : p.getNearbyEntities (4D, 4D, 4D)){
			if (en instanceof Player){
				if(en == p) continue;
				if(e.getBucket() == Material.LAVA_BUCKET){
					
					if(!Files.user.getBoolean("Users." + en.getName() + ".pvp")){
							p.sendMessage(Message.prefix + ChatColor.DARK_RED + " No puedes poner ese bloque cerca de un jugador con el pvp desactivado.");
							e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		
		Player p = e.getPlayer();
		
		if(manager.isInPvP(p)){
			e.setCancelled(true);
			p.sendMessage(Message.prefix + ChatColor.DARK_RED + " ¡No puedes ejecutar comandos en pvp!");
		}
		
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		Player p = event.getPlayer();

		if(Files.user.contains("Users." + p.getName())){

			try{
				Files.user.save(Files.users);
				Files.user.load(Files.users);
			}catch(IOException | InvalidConfigurationException e){
				e.printStackTrace();
			}
			
			if(manager.isInPvP(p)){
				p.setHealth(0D);
				Bukkit.broadcastMessage(Message.prefix + ChatColor.GRAY + " ¡" + ChatColor.GOLD + p.getName() + Message.leave_in_pvp);
			}
		}
	}
}
