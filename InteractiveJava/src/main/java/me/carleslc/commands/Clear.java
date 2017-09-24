package me.carleslc.commands;

import me.carleslc.interactiveJava.Command;
import me.carleslc.interactiveJava.console.Console;

public class Clear implements Command {

	@Override
	public String execute(Console console, String[] args) {
		console.clear();
		return null;
	}
	
	@Override
	public String help() {
		return "Clears the console.";
	}

}
