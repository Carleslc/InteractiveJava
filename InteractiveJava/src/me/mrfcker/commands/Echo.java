package me.mrfcker.commands;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;

public class Echo implements Command {

	@Override
	public Object execute(Console console, String[] args) {
		if (args.length > 0) {
			if (args[1].equalsIgnoreCase("off"))
				console.enablePrinting(false);
			else if (args[1].equalsIgnoreCase("on"))
				console.enablePrinting(true);
			else
				console.printAsConsole(args[1]);
			return args[1];
		}
		else
			console.printAsHelp(help());
		return null;
	}

	@Override
	public String help() {
		return "echo {msg} - Prints a message.\n"
				+ "echo off - Disable console output.\n"
				+ "echo on - Enable console output.";
	}

}
