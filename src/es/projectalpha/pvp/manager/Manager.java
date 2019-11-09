package es.projectalpha.pvp.manager;


import es.projectalpha.pvp.PvpManager;
import es.projectalpha.pvp.files.Files;
import org.bukkit.entity.Player;


public class Manager {

	public void setPvPState(Player p, boolean pvp){
		if(checkStatus(p, pvp)) return;
		Files.user.set("Users." + p.getName() + ".pvp", pvp);
		addCooldown(p);
	}

	public void setPvPStateAdmin(Player p, boolean pvp){
		if(checkStatus(p, pvp)) return;
		Files.user.set("Users." + p.getName() + ".pvp", pvp);
	}

	private boolean getPvPState(Player p){
		return Files.user.getBoolean("Users." + p.getName() + ".pvp");
	}

	private boolean checkStatus(Player p, boolean pvp){
		return pvp == getPvPState(p);
	}

	public String parseStatus(Player p){
		if(getPvPState(p)){
			return "true";
		}
		return "false";
	}

	public void addCooldown(Player p){
		if(isInCooldown(p)) return;

		PvpManager.getInstance().cooldown.put(p, 20); //Tiempo en segundos
	}

	public boolean isInCooldown(Player p){
		return PvpManager.getInstance().cooldown.containsKey(p);
	}

	public void removeCooldown(Player p){
		if(!isInCooldown(p)) return;
		PvpManager.getInstance().cooldown.remove(p);
	}

	public void addPvp(Player p){
		if(isInPvP(p)) return;

		PvpManager.getInstance().pvpCooldown.put(p, 15);

	}

	public boolean isInPvP(Player p){
		return PvpManager.getInstance().pvpCooldown.containsKey(p);

	}

	public void addNewbie(Player p){
		if(isInPvP(p)) return;

		PvpManager.getInstance().newbieCooldown.put(p, 600);

	}

	public boolean isNewbie(Player p){
		return PvpManager.getInstance().newbieCooldown.containsKey(p);
	}
}
