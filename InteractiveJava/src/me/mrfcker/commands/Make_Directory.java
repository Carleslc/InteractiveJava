package me.mrfcker.commands;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;

public class Make_Directory implements Command {
	
	@Override
	public String help() {
		return "mkdir {dir} - Creates a new directory on the current directory (PWD).";
	}

	@Override
	public Object execute(Console console, String[] args) {
		if (args.length > 1) {
			args[1] = "/" + args[1];
			new Touch().execute(console, args);
		}
		else
			console.printAsHelp(help());
		return null;
	}
	
}
