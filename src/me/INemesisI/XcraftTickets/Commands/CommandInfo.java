package me.INemesisI.XcraftTickets.Commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

	/**
	 * the command this sub-command is executed on
	 */
	public String command();

	/**
	 * A pattern, this command is executed on
	 */
	public String pattern();

	/**
	 * the permisson node needed for that command
	 */
	public String permission();

	/**
	 * A User-Friendly name of the Command
	 */
	public String name();

	/**
	 * A User-Friendly usage description for example /plugin reload
	 */
	public String usage();

	/**
	 * A User-Friendly description of the command
	 */
	public String desc();

}