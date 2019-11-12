package me.wikyfg.pvp.events;

import me.wikyfg.pvp.PvpManager;
import me.wikyfg.pvp.files.Files;
import me.wikyfg.pvp.files.Message;
import me.wikyfg.pvp.manager.Manager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class EntityEvent implements Listener {

    private PvpManager plugin;
    private Manager manager;

    
    public EntityEvent(PvpManager PvpManager, Manager manager){
        this.plugin = PvpManager;
        this.manager = manager;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){
    	final Entity damager = e.getDamager();
        final Entity en = e.getEntity();
        
    	if (damager instanceof Projectile){
            if (en instanceof Player){
                final Projectile pr = (Projectile)damager;
                
            	if(!(pr.getShooter() instanceof Player)) return;
                
                final Player p = (Player)pr.getShooter();
                final Player pl = (Player)en;

                if (p.hasMetadata("NPC") || pl.hasMetadata("NPC")) return;
                
                if(p == pl) return;
                if(Files.user.getBoolean("Users." + p.getName() + ".pvp") && Files.user.getBoolean("Users." + pl.getName() + ".pvp")){
                	
                	if(p.getGameMode() == GameMode.CREATIVE)p.setGameMode(GameMode.SURVIVAL);
                	
                	if(p.isFlying()) p.setFlying(false);
                	
                	if(p.hasPotionEffect(PotionEffectType.INVISIBILITY)) p.getActivePotionEffects().clear();
                	
                    if(manager.isInPvP(pl) || manager.isNewbie(pl) || manager.isInPvP(p) || manager.isNewbie(p)) return;
                    
                    manager.addPvp(p);
                    manager.addPvp(pl);

                    p.sendMessage(Message.prefix + ChatColor.DARK_RED + " Has entrado en pelea con " + ChatColor.DARK_GRAY + pl.getName() + ChatColor.DARK_RED + ", �no te desconectes!");
                    pl.sendMessage(Message.prefix + ChatColor.DARK_RED + "Has entrado en pelea con " + ChatColor.DARK_GRAY + p.getName() + ChatColor.DARK_RED + ", �no te desconectes!");

                }else{
                    e.setCancelled(true);
                    e.setDamage(0D);
                    pl.setFireTicks(0);
                    
                    if(manager.isNewbie(p)){
                    	p.sendMessage(Message.prefix + ChatColor.DARK_GRAY + " Tienes la protección de novato activada, para desactivarla haz /pvp disable.");
                    	return;
                    }
                    
                    if(manager.isNewbie(pl)){
                    	p.sendMessage(Message.prefix + " " + ChatColor.GOLD + pl.getName() + ChatColor.DARK_GRAY + " tiene la protección de novato activada.");
                    	return;
                    }
                    
                    if(Files.user.getBoolean("Users." + p.getName() + ".pvp")){
                        p.sendMessage(Message.prefix + ChatColor.DARK_GRAY + " Tienes el pvp desactivado.");
                        return;
                    }

                    if(Files.user.getBoolean("Users." + pl.getName() + ".pvp")){
                        p.sendMessage(Message.prefix + ChatColor.GOLD + pl.getName() + ChatColor.DARK_GRAY + " tiene el pvp desactivado.");
                        return;
                    }
                }
            }
        }

        if(damager instanceof Player){
            if(en instanceof  Player){
                Player p = (Player)damager;
                Player pl = (Player)en;

                if (p.hasMetadata("NPC") || pl.hasMetadata("NPC")) return;
                
                if(Files.user.getBoolean("Users." + p.getName() + ".pvp") && Files.user.getBoolean("Users." + pl.getName() + ".pvp")){
                
                	if(p.getGameMode() == GameMode.CREATIVE){
                    	p.setGameMode(GameMode.SURVIVAL);
                    }

                    if(manager.isInPvP(pl) || manager.isNewbie(pl) || manager.isInPvP(p) || manager.isNewbie(p)) return;
                	
                	
                	manager.addPvp(p);
                	manager.addPvp(pl);
                
                	p.sendMessage(Message.prefix + ChatColor.DARK_RED + " Has entrado en pelea con " + ChatColor.DARK_GRAY + pl.getName());
                    pl.sendMessage(Message.prefix + ChatColor.DARK_RED + " Has entrado en pelea con " + ChatColor.DARK_GRAY + p.getName());
                
                }else{
                    e.setCancelled(true);
                    e.setDamage(0D);
                    pl.setFireTicks(0);
                    
                    if(manager.isNewbie(p)){
                    	p.sendMessage(Message.prefix + ChatColor.DARK_GRAY + " Tienes la protección de novato activada, para desactivarla haz /pvp disable.");
                    	return;
                    }
                    
                    if(manager.isNewbie(pl)){
                    	p.sendMessage(Message.prefix + " " + ChatColor.GOLD + pl.getName() + ChatColor.DARK_GRAY + " tiene la protección de novato activada.");
                    	return;
                    }
                    
                    if(Files.user.getBoolean("Users." + p.getName() + ".pvp")){
                        p.sendMessage(Message.prefix + ChatColor.DARK_GRAY + " Tienes el pvp desactivado.");
                        return;
                    }

                    if(Files.user.getBoolean("Users." + pl.getName() + ".pvp")){
                        p.sendMessage(Message.prefix + ChatColor.GOLD + pl.getName() + ChatColor.DARK_GRAY + " tiene el pvp desactivado.");
                    }
                }
            }
        }
    	
    }
}