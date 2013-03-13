package me.INemesisI.XcraftTickets;

import org.bukkit.ChatColor;

public class Log {
	public String date;
	public String player;
	public String message;
	public Type type;

	public enum Type {
		OPEN, COMMENT, CLOSE, REOPEN, ASSIGN;
	}

	public Log(String date, String player, Type type, String message) {
		this.date = date;
		this.player = player;
		this.type = type;
		this.message = message;
	}

	@Override
	public String toString() {
		return date + "; " + player + "; " + type + "; " + message;
	}

	public String format() {
		switch (type) {
			case OPEN :
				return ChatColor.GOLD + "Erstellt: " + ChatColor.GRAY + date + ChatColor.WHITE + " | "
						+ ChatColor.YELLOW + player + ChatColor.WHITE + ": " + message;
			case COMMENT :
				return ChatColor.BLUE + "-> " + ChatColor.DARK_GRAY + date + ChatColor.WHITE + " | " + ChatColor.YELLOW
						+ player + ChatColor.WHITE + ": " + message;
			case CLOSE :
				return ChatColor.RED + "Geschlossen: " + ChatColor.DARK_GRAY + date + ChatColor.WHITE + " | "
						+ ChatColor.YELLOW + player + ChatColor.WHITE + ": " + message;
			case REOPEN :
				return ChatColor.GREEN + "Geoeffnet: " + ChatColor.DARK_GRAY + date + ChatColor.WHITE + " | "
						+ ChatColor.YELLOW + player + ChatColor.WHITE + ": " + message;
			case ASSIGN :
				return ChatColor.GREEN + "Weitergeleitet: " + ChatColor.DARK_GRAY + date + ChatColor.WHITE + " | von "
						+ ChatColor.YELLOW + player + ChatColor.WHITE + " nach " + message;
		}
		return "";

	}
}
