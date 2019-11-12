package me.wikyfg.pvp.files;

import org.bukkit.ChatColor;

import java.util.Objects;

public class Message {

	public static String console = new Message("lang.console").getMsg();
	public static String noperm = new Message("lang.noperm").getMsg();
	public static String help = new Message("lang.help").getMsg();
	public static String pvp_on = new Message("lang.pvp_on").getMsg();
	public static String pvp_off = new Message("lang.pvp_off").getMsg();
	public static String leave_in_pvp = new Message("lang.leave_in_pvp").getMsg();
	public static String pvp_on_error = new Message("lang.pvp_on_error").getMsg();
	public static String pvp_off_error = new Message("lang.pvp_off_error").getMsg();
	public static String player_not_exist = new Message("lang.pvp_off_error").getMsg();
	public static String prefix = new Message("lang.prefix").getMsg();

	private final String msg;

	private Message(String path) {
		msg = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Files.clang.getString(path)));
	}

	public String getMsg() {
		return msg;
	}
}
