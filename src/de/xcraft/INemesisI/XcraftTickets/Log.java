package de.xcraft.INemesisI.XcraftTickets;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.xcraft.INemesisI.XcraftTickets.Msg.Replace;

public class Log {
	private final DateFormat formatter;
	private final List<LogEntry> entries;

	public Log(DateFormat formatter) {
		this.formatter = formatter;
		this.entries = new ArrayList<LogEntry>();
	}

	public void add(EntryType type, String player, String message) {
		long time = (new Date()).getTime();
		entries.add(new LogEntry(time, type, player, message));
	}

	public void add(long time, EntryType type, String player, String message) {
		entries.add(new LogEntry(time, type, player, message));

	}

	public LogEntry getEntry(int index) {
		return entries.get(index);
	}

	public boolean remove(LogEntry entry) {
		return entries.remove(entry);
	}

	public int size() {
		return entries.size();
	}

	public String getDate() {
		return formatter.format(new Date(entries.get(0).time));
	}

	public String getEntryOutput(int index) {
		LogEntry entry = entries.get(index);
		Replace[] replace = { Replace.NAME(entry.player), Replace.TIME(formatter.format(new Date(entry.time))), Replace.MESSAGE(entry.message) };
		switch (entry.type) {
		case OPEN:
			return Msg.TICKET_VIEW_OPEN.toString(replace);
		case COMMENT:
			return Msg.TICKET_VIEW_COMMENT.toString(replace);
		case CLOSE:
			return Msg.TICKET_VIEW_CLOSE.toString(replace);
		case REOPEN:
			return Msg.TICKET_VIEW_REOPEN.toString(replace);
		case ASSIGN:
			replace[2] = Replace.ASSIGNEE(entry.message);
			return Msg.TICKET_VIEW_ASSIGN.toString(replace);
		case SETWARP:
			return Msg.TICKET_VIEW_SETWARP.toString(replace);
		}
		return null;
	}

	public String[] getEntries() {
		String[] list = new String[entries.size()];
		for (int i = 0; i < entries.size(); i++) {
			list[i] = getEntryOutput(i);
		}
		return list;
	}

	public enum EntryType {
		OPEN, COMMENT, CLOSE, REOPEN, ASSIGN, SETWARP;
	}

	public class LogEntry {
		public final long time;
		public final String player;
		public final String message;
		public final EntryType type;

		public LogEntry(long time, EntryType type, String player, String message) {
			this.time = time;
			this.player = player;
			this.message = message;
			this.type = type;
		}

		@Override
		public String toString() {
			return time + "; " + player + "; " + type.name() + "; " + message;
		}
	}
}
