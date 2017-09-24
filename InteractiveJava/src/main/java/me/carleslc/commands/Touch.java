package me.carleslc.commands;

import java.io.File;
import java.io.IOException;

import me.carleslc.interactiveJava.Command;
import me.carleslc.interactiveJava.console.Console;
import me.carleslc.utils.StringUtils;

public class Touch implements Command {

	@Override
	public String execute(Console console, String[] args) {
		if (args.length > 1) {
			File f = new File(console.getVariable("PWD") + StringUtils.concat(args, 1));
			if (f.exists())
				console.printError(f.getName() + " already exists!");
			else {
				try {
					if (args[1].startsWith("/") || args[1].startsWith(File.separator))
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
