package me.mrfcker.commands;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;

public class Exit implements Command {

	@Override
	public Object execute(Console console, String[] args) {
		console.getRootFrame().dispose();
		return null;
	}

	@Override
	public String help() {
		return "Closes the Interactive Java Console.";
	}

}
