package me.carleslc.commands;

import me.carleslc.interactiveJava.Command;
import me.carleslc.interactiveJava.console.Console;

public class Make_Directory implements Command {
	
	@Override
	public String help() {
		return "mkdir {dir} - Creates a new directory on the current directory (PWD).";
	}

	@Override
	public String execute(Console console, String[] args) {
		if (args.length > 1) {
			args[1] = "/" + args[1];
			new Touch().execute(console, args);
		}
		else
			console.printAsHelp(help());
		return null;
	}
	
}
