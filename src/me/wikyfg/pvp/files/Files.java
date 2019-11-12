package me.wikyfg.pvp.files;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Files {
	
	public static File lang = new File("plugins/PvPManager/", "lang.yml");
	public static File users = new File("plugins/PvPManager/", "users.yml");
	public static YamlConfiguration clang = YamlConfiguration.loadConfiguration(lang);
	public static YamlConfiguration user = YamlConfiguration.loadConfiguration(users);
	public static File config = new File("plugins/PvPManager/", "config.yml");
	public static YamlConfiguration cfile = YamlConfiguration.loadConfiguration(config);
	private Lang slang = new Lang();
    public void setupFiles(){
    	
    	if(!lang.exists()){
    		lang.mkdir();
    		slang.setupLang();
    	}
    	
    	if(!users.exists()){
    		users.mkdir();
    	}

    	saveFiles();
    	if(!config.exists()){
    		config.mkdir();
		}
    }


    private void saveFiles(){
		try{
			clang.save(lang);
			clang.load(lang);
			user.save(users);
			user.load(users);
		}catch(IOException | InvalidConfigurationException e){
			e.printStackTrace();
		}
	}

}
