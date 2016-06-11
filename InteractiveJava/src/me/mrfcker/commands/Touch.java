package me.mrfcker.commands;

import java.io.File;
import java.io.IOException;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;
import me.mrfcker.utils.StringUtils;

public class Touch implements Command {

	@Override
	public Object execute(Console console, String[] args) {
		if (args.length > 1) {
			File f = new File(console.getVariable("PWD") + StringUtils.concat(args, 1));
			if (f.exists())
				console.printError(f.getName() + " already exists!");
			else {
				try {
					if (args[1].startsWith("/") || args[1].startsWith("\\"))
						f.mkdir();
					else
						f.createNewFile();
					console.printAsHelp(f.getName() + " created successfully.");
					return args[1];
				} catch (IOException e) {
					console.printError(f.getName() + " cannot be created.");
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
