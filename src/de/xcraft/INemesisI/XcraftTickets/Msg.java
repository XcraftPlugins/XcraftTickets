package de.xcraft.INemesisI.XcraftTickets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import de.xcraft.INemesisI.Utils.XcraftPlugin;
import de.xcraft.INemesisI.Utils.Message.Messenger;

public enum Msg {
	//@formatter:off
	//general stuff
	HELP("&eAvaliable commands:"), 
	NONE("none"),
	ERR_NO_ACCESS("&cYou don't have access to this command."), 
	ERR_NOT_FROM_CONSOLE("&cYou can't use this command from the console."), 
	ERR_MULTIPLE_MATCHES("&cDid you mean one of these commands?"), 
	ERR_NO_MATCHES("&cCommand $MISC$ not found."), 
	ERR_USAGE("&cUsage: "),
	//Plugin specific errors
	ERR_NO_TICKET_ID("You need to provide a ticket-id."),
	ERR_NO_MESSAGE("You need to provide a message."), 
	ERR_TICKET_NOT_FOUND("Could not find a ticket with the id $ID$!"),
	ERR_TICKET_NO_PERMISSION("You dont have permission to access that ticket!"),
	ERR_MOD_NOT_FOUND("Could not find an assignee with that name: $NAME$."),
	ERR_ASSIGNEE_NOT_FOUND("A assignee with the name $name$ does not exist!"),
	ERR_PHRASE_NOT_FOUND("Could not find an phrase with that name: $NAME$."),
	ERR_UNDO_IMPOSSIBLE("This undo is impossible, sorry."),
	//Admin Commands
	COMMAND_SAVE("Save all data to disc."),
	COMMAND_SAVE_SUCCESSFUL("Successfully saved to disc"),
	COMMAND_RELOAD("Load all data from disc."),
	COMMAND_RELOAD_SUCCESSFUL("Successfully loaded from disc."), 
	COMMAND_MOD("Edit all assignees."),
	COMMAND_MOD_LIST("current assignees: $MISC$."),
	COMMAND_MOD_ADD("Successfully added the assignee $NAME$."),
	COMMAND_MOD_REMOVE("Successfully removed the assignee $NAME$."),
	COMMAND_STATS("Show current Ticket-Stats."),
	//Mod Commands
	COMMAND_ASSIGN("Assign a ticket."),
	COMMAND_PHRASES("Edit all phrases."),
	COMMAND_PHRASES_LIST("current phrases: $MISC$"),
	COMMAND_PHRASES_ADD("Successfully added the phrase $NAME$: $MESSAGE$"),
	COMMAND_PHRASES_REMOVE("Successfully removed the phrase $NAME$"),
	COMMAND_PHRASES_APPEND("Successfully extended the phrase $NAME$: $MESSAGE$"),
	COMMAND_UNASSIGN("Save all data to disc."),
	COMMAND_UNDO("Revokes the last action."),
	COMMAND_UNDO_SUCCESSFUL("Undo successful"),
	COMMAND_WARP("Warp to a ticket."),
	//Player Commands
	COMMAND_CLOSE("Close a ticket."),
	COMMAND_LIST("List all tickets."),
	COMMAND_LOG("Comment to a ticket."),
	COMMAND_OPEN("Open a ticket."),
	COMMAND_OPEN_SUCCESSFUL("Thank you, $name$! Your ticket-id is #$ID$! A Admin/Mod will deal with you ticket soon!"),
	COMMAND_REOPEN("Re-open a closed ticket."),
	COMMAND_SETWARP("Change the warp of a ticket."),
	COMMAND_VIEW("Show all information of a ticket."),
	//Command usage
	USAGE_ID("#"),
	USAGE_MESSAGE("Message"),
	USAGE_ASSIGNEE("Name/Group"),
	//Ticket broadcast
	TICKET_BROADCAST_ASSIGN("Ticket #$ID$ was assigned to $NAME$"),
	TICKET_BROADCAST_UNASSIGN("The assignment of ticket #$ID$ has been removed!"),
	TICKET_BROADCAST_WARP("$NAME$ is processing ticket #$ID$"),
	TICKET_BROADCAST_CLOSE("Ticket #$ID$ was closed by $NAME$: $MESSAGE$"),
	TICKET_BROADCAST_COMMENT("Ticket #$ID$ was commented by $NAME$: $MESSAGE$"),
	TICKET_BROADCAST_OPEN("Ticket #$ID$ was opened by $NAME$: $MESSAGE$"),
	TICKET_BROADCAST_REOPEN("Ticket #$ID$ was reopened by $NAME$: $MESSAGE$"),
	TICKET_BROADCAST_SETWARP("The warp of Ticket #$ID$ was updated by $NAME$"),
	//Ticket Info display
	TICKET_VIEW_INFO("&3Info for ticket &6#$ID$ &3from &e$NAME$ &3$ASSIGNEE$"),
	TICKET_VIEW_BREAK("--------------------------------------------------"),
	TICKET_VIEW_OPEN("&8$TIME$&f | &aopened &fby &e$NAME$&f: $MESSAGE$"),
	TICKET_VIEW_COMMENT("&8$TIME$&f | &e$NAME$&f: $MESSAGE$"),
	TICKET_VIEW_CLOSE("&8$TIME$&f | &cclosed &fby &e$NAME$&f: $MESSAGE$"),
	TICKET_VIEW_SETWARP("&8$TIME$&f | &bwarp updated &fby &e$NAME$&f: $MESSAGE$"),
	TICKET_VIEW_REOPEN("&8$TIME$&f | &9reopened &fby &e$NAME$&f: $MESSAGE$"),
	TICKET_VIEW_ASSIGN("&8$TIME$&f | &5assigend &fby &e$NAME$&f: to &5$ASSIGNEE$"),
	//Ticket list display
	TICKET_LIST_HEAD("Ticketlist:"),
	TICKET_LIST_EMTPY("There are no open tickets..."),
	TICKET_LIST_MISC_ONLINE("&f[&2+&f]"),
	TICKET_LIST_MISC_OFFLINE("&f[&4-&f]"),
	TICKET_LIST("&6#$ID$ &8$TIME$ $MISC$ &e$NAME$ &7$ASSIGNEE$&f\n&f&o$MESSAGE$"),
	//Remind...
	TICKET_REMIND_CLOSE("Your ticket $ID$ was closed. Please revisit! (/ticket view $ID$)"),
	TICKET_REMIND_UNREAD("You have unread messages in you Ticket $ID$! (/ticket view $ID$)"),
	TICKET_REMIND_UNREAD_LIST("You have $MISC$ unread Tickets! (/ticket list)"),

	TICKET_ASSIGNEE("assigned to &5$NAME$");
	//@formatter:on

	public enum Replace {
		$NAME$("Name a Player"), $ID$("ID of a ticket"), $MESSAGE$("Message provided in a command"), $TIME$("timesamp for a ticket"), $ASSIGNEE$(
				"Assignee of a ticket"), $MISC$("Miscellaneous stuff");

		private String key;

		Replace(String key) {
			this.set(key);
		}

		private void set(String output) {
			key = output;
		}

		private String get() {
			return key;
		}

		public static Replace NAME(String replace) {
			$NAME$.set(replace);
			return $NAME$;
		}

		public static Replace ID(int replace) {
			$ID$.set(String.valueOf(replace));
			return $ID$;
		}

		public static Replace MESSAGE(String replace) {
			$MESSAGE$.set(replace);
			return $MESSAGE$;
		}

		public static Replace TIME(String replace) {
			$TIME$.set(replace);
			return $TIME$;
		}

		public static Replace ASSIGNEE(String replace) {
			$ASSIGNEE$.set(replace);
			return $ASSIGNEE$;
		}

		public static Replace MISC(String replace) {
			$MISC$.set(replace);
			return $MISC$;
		}
	}

	private String msg;

	Msg(String msg) {
		this.set(msg);
	}

	private void set(String output) {
		msg = output;
	}

	private String get() {
		return msg;
	}

	@Override
	public String toString() {
		String message = msg.replaceAll("&([0-9a-z])", "\u00a7$1");
		message = message.replace("\\n", "\n");
		return message;
	}

	public String toString(Replace r1) {
		String message = toString();
		message = message.replace(r1.name(), r1.get());
		return message;
	}

	public String toString(Replace r1, Replace r2) {
		String message = toString();
		message = message.replace(r1.name(), r1.get());
		message = message.replace(r2.name(), r2.get());
		return message;
	}

	public String toString(Replace[] repl) {
		String message = toString();
		for (Replace r : repl) {
			message = message.replace(r.name(), r.get());
		}
		return message;
	}

	public static void init(XcraftPlugin plugin) {
		File msgFile = new File(plugin.getDataFolder(), "locale.yml");
		if (!load(msgFile)) {
			return;
		}
		parseFile(msgFile);
	}

	private static boolean load(File file) {
		if (file.exists()) {
			return true;
		}
		try {
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (Msg m : Msg.values()) {
				String msg = m.get();
				if (msg.contains("\n")) {
					msg = msg.replace("\n", "\\n");
				}
				bw.write(m.name() + ": " + msg);
				bw.newLine();
			}
			bw.close();
			return true;
		} catch (Exception e) {
			Messenger.warning("Couldn't initialize locale.yml. Using defaults.");
			return false;
		}
	}

	private static void parseFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			// Check for BOM character.
			br.mark(1);
			int bom = br.read();
			if (bom != 65279) {
				br.reset();
			}
			String s;
			while ((s = br.readLine()) != null) {
				process(s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			Messenger.warning("Problem with locale.yml. Using defaults.");
			return;
		}
	}

	/**
	 * Helper-method for parsing the strings from the announcements-file.
	 */
	private static void process(String s) {
		String[] split = s.split(": ", 2);
		try {
			Msg msg = Msg.valueOf(split[0]);
			msg.set(split[1]);
		} catch (Exception e) {
			Messenger.warning(split[0] + " is not a valid key. Check locale.yml.");
			return;
		}
	}

}
