package me.mrfcker.commands;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;

public class Clear implements Command {

	@Override
	public Object execute(Console console, String[] args) {
		console.clear();
		return null;
	}
	
	@Override
	public String help() {
		return "Clears the console.";
	}

}
