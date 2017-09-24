package me.carleslc.commands;

import me.carleslc.interactiveJava.Command;
import me.carleslc.interactiveJava.console.Console;

public class Exit implements Command {

	@Override
	public String execute(Console console, String[] args) {
		console.dispose(console.getRootFrame());
		System.exit(0);
		return null;
	}

	@Override
	public String help() {
		return "Closes the Interactive Java Console.";
	}

}
