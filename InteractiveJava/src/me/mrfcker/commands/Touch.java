package me.mrfcker.commands;

import java.io.File;
import java.io.IOException;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;

public class Touch implements Command {

	@Override
	public Object execute(Console console, String[] args) {
		if (args.length > 1) {
			File f = new File(console.getVariable("PWD") + args[1]);
			if (f.exists())
				console.printError(args[1] + " already exists!");
			else {
				try {
					f.createNewFile();
					return args[1];
				} catch (IOException e) {
					console.printError("File cannot be created.");
					e.printStackTrace();
				}
			}
		}
		else
			console.printAsHelp(help());
		return null;
	}

	@Override
	public String help() {
		return "touch {file} - Creates a new file on the current directory (PWD).";
	}

}
