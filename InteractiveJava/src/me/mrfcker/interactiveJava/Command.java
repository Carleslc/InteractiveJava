package me.mrfcker.interactiveJava;

import java.util.HashMap;
import java.util.Map;

import me.mrfcker.exceptions.AlreadyExistsException;
import me.mrfcker.interactiveJava.console.Console;
import me.mrfcker.utils.ReflectionClassUtils;
import me.mrfcker.utils.StringUtils;

public interface Command {

	static Map<String, Command> alias = new HashMap<>();
	
	static Command get(String command) throws Exception {
		command = StringUtils.formatAsCommand(command);
		Command alias = Command.alias.get(command);
		if (alias != null)
			return alias;
		Class<?> c = ReflectionClassUtils.forName("me.mrfcker.commands." + command);
		if (c == null)
			c = ReflectionClassUtils.forName("me.mrfcker.commands.games." + command);
		if (c == null)
			throw new ClassNotFoundException(command + " not found.");
		return (Command)c.newInstance();
	}
	
	public static void addAlias(String command, String alias) throws Exception {
		get(command).addAlias(alias);
	}
	
	default void addAlias(String alias) throws Exception {
		if (alias != null && !alias.isEmpty()) {
			try {
				get(alias);
			} catch (Exception e) {
				Command.alias.put(StringUtils.formatAsCommand(alias), this);
				return;
			}
			// if already exists (no ClassNotFoundException is thrown)
			throw new AlreadyExistsException(alias + " already exists as a command or alias!");
		}
	}
	
	default String getName() {
		return getClass().getSimpleName().toLowerCase();
	}
	
	Object execute(Console console, String[] args);
	
	String help();
}
