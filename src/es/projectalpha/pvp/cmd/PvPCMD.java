package es.projectalpha.pvp.cmd;


import es.projectalpha.pvp.files.Message;
import es.projectalpha.pvp.PvpManager;
import es.projectalpha.pvp.files.Files;
import es.projectalpha.pvp.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;

public class PvPCMD implements CommandExecutor {

	private Manager manager = PvpManager.getInstance().getManager();

	private void save() {
		try {
			Files.user.save(Files.users);
			Files.user.load(Files.users);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player user = (Player) sender;
		if(args.length == 0){
			if(!manager.isInCooldown(user.getPlayer())){
				manager.addCooldown(user.getPlayer());
				if(Files.user.getBoolean("Users." + user.getName() + ".pvp") == true){
					Files.user.set("Users." + user.getName() + ".pvp", false);
					user.sendMessage(Message.prefix + ChatColor.GREEN + " Has desactivado tu pvp.");
					return false;
				}

				if(Files.user.getBoolean("Users." + user.getName() + ".pvp") == false){
					Files.user.set("Users." + user.getName() + ".pvp", true);
					user.sendMessage(Message.prefix + ChatColor.DARK_RED + " Has activado tu pvp.");
					return false;
				}

			}else{
				user.sendMessage(Message.prefix + ChatColor.DARK_RED + " Tienes que esperar para usar este comando.");
			}
		}

		if(args.length == 1){

			if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("info") || args[0].equals("reload")){

				if(args[0].equalsIgnoreCase("help")){
					user.sendMessage(ChatColor.GRAY + "<--------------------------------------------------->");
					user.sendMessage(ChatColor.GOLD + "/pvp. " + ChatColor.GREEN + "Sirve para cambiarte el estado del pvp, hay un subcomando, el cual es /pvp <on/off> que hace lo mismo.");
					user.sendMessage(ChatColor.GOLD + "/pvp status. " + ChatColor.GREEN + "Con este comando puedes ver si tienes el pvp activado o desactivado.");
					user.sendMessage(ChatColor.GOLD + "/pvp info. " + ChatColor.GREEN + "Este comando te da un poco de información sobre el servidor.");
					if(user.hasPermission("admin")){
						user.sendMessage("");
						user.sendMessage(ChatColor.RED + "Comandos de administrador");
						user.sendMessage("");
						user.sendMessage(ChatColor.GOLD + "/pvp set <nombre> <on/off>." + ChatColor.GREEN + " Con este comando puedes activar o desactivar el pvp a un jugador.");
						user.sendMessage(ChatColor.GOLD + "/pvp inspect <nombre>." + ChatColor.GREEN + " Usando este comando puedes ver si una persona tiene el pvp activado o desactivado.");
					}
					user.sendMessage(ChatColor.GRAY + "<--------------------------------------------------->");
				}


				if(args[0].equalsIgnoreCase("on")){
					if(!manager.isInCooldown(user.getPlayer())){
						if(manager.parseStatus(user.getPlayer()).equalsIgnoreCase("false")){
							manager.setPvPState(user.getPlayer(), true);
							user.sendMessage(Message.pvp_on);
							save();
							return false;
						}else{
							user.sendMessage(Message.pvp_on_error);
						}
					}
				}

				if(args[0].equalsIgnoreCase("off")){
					if(!manager.isInCooldown(user.getPlayer())){
						if(manager.parseStatus(user.getPlayer()).equalsIgnoreCase("true")){
							manager.setPvPState(user.getPlayer(), false);
							user.sendMessage(Message.pvp_off);
							save();
							return false;
						}else{
							user.sendMessage(Message.pvp_off_error);
						}
					}else{
						user.sendMessage(Message.prefix + ChatColor.DARK_RED + " Tienes que esperar para usar este comando.");
					}
				}

				if(args[0].equalsIgnoreCase("status")){
					if(manager.parseStatus(user.getPlayer()).equalsIgnoreCase("true")){
						user.sendMessage(ChatColor.RED + "Actualmente, tu pvp está activado");
					}else{
						user.sendMessage(ChatColor.GREEN + "Actualmente, tu pvp está desactivado");
					}
				}

				if(args[0].equalsIgnoreCase("disable")){
					if(manager.isNewbie(user.getPlayer())){
						PvpManager.getInstance().newbieCooldown.remove(user);
						user.sendMessage(Message.prefix + ChatColor.GRAY + " Has desactivado tu protección de novato, ahora puedes ser golpeado por otros jugadores");
					}else{
						user.sendMessage(Message.prefix + ChatColor.DARK_RED + " Ya has desactivado tu protección de novato, no puedes hacerlo de nuevo.");
					}
				}

				if(args[0].equalsIgnoreCase("info")){
					user.sendMessage(ChatColor.GRAY + "<--------------------------------------------------->");
					user.sendMessage(ChatColor.GOLD + "Plugin creado por: " + ChatColor.GREEN + "Wikijito7");
					user.sendMessage(ChatColor.GOLD + "Ayudado por: " + ChatColor.GREEN + "Cadox8");
					user.sendMessage(ChatColor.GOLD + "Versión del plugin: " + ChatColor.GREEN + "1.2.2");
					user.sendMessage(ChatColor.GOLD + "Copyright: " + ChatColor.GREEN + "ProjectAlphaDevs 2016");
					user.sendMessage(ChatColor.GRAY + "<--------------------------------------------------->");
				}

			}else{
				user.sendMessage(Message.prefix + " " + Message.help);
			}
		}


		if(args.length == 2){
			if(args[0].equalsIgnoreCase("inspect")){
				if(user.hasPermission("pvpInspect")){

					Player pl = Bukkit.getPlayerExact(args[1]);

					if(pl != null){
						user.sendMessage(ChatColor.GREEN + "Ahora, " + ChatColor.AQUA + pl.getName() + ChatColor.GREEN + " tiene el pvp " + ChatColor.YELLOW + Files.user.get("Users." + pl.getName() + ".pvp"));

					}else{
						user.sendMessage(Message.player_not_exist);
					}

				}else{
					user.sendMessage(Message.noperm);
				}

			}else{
				user.sendMessage(Message.prefix + " " + Message.help);
			}

		}

		if(args.length == 3){
			if(args[0].equalsIgnoreCase("set")){
				if(!user.hasPermission("pvpSet")){
					user.sendMessage(Message.noperm);
					return false;
				}

				Player pl = Bukkit.getPlayerExact(args[1]);

				if(pl == null){
					user.sendMessage(Message.player_not_exist);
					return false;
				}

				switch(args[2].toLowerCase()){ //Por si es en mayusculas
					case "on":
						manager.setPvPStateAdmin(pl, true);
						user.sendMessage(Message.prefix +ChatColor.DARK_RED + "Has activado el pvp de " + pl.getName());
						return false;
					case "off":
						manager.setPvPStateAdmin(pl, false);
						user.sendMessage(Message.prefix + ChatColor.GREEN + " Has desactivado el pvp de " + pl.getName());
						return false;
					default:
						user.sendMessage(ChatColor.RED + "Solo puedes poner on o off");
						return false;
				}
			}else{
				user.sendMessage(Message.prefix + " " + Message.help);
			}
		}

		if(args.length >= 4){
			user.sendMessage(Message.prefix + " " + Message.help);
		}
		return false;
	}
}