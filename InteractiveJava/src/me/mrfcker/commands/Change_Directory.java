package me.mrfcker.commands;

import java.io.File;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;

public class Change_Directory implements Command {

	@Override
	public String execute(Console console, String[] args) {
		if (args.length > 1) {
			String path = console.getVariable("PWD").toString();
			if (args[1].equals("..")) {
				File dir = new File(path);
				String parent = dir.getParent();
				if (parent != null) {
					path = parent + File.separatorChar;
					console.addSystemVariable("PWD", path, true);
					console.getRootFrame().setTitle(path);
				}
				else
					console.printAsHelp("This is the root directory.");
				return console.getVariable("PWD").toString();
			}
			else {
				path += args[1];
				File dir = new File(path);
				if (dir.exists()) {
					if (dir.isDirectory()) {
						path += File.separatorChar;
						console.addSystemVariable("PWD", path, true);
						console.getRootFrame().setTitle(path);
						return path;
					}
					else
						console.printError(args[1] + " must be a directory.");
				}
				else
					console.printError(args[1] + " not exists.");
			}
		}
		else
			console.printAsHelp(help());
		return null;
	}

	@Override
	public String help() {
		return "cd {DIR} - Changes current directory (PWD) to {DIR}.\n"
				+ "cd .. - Changes current directory (PWD) to parent directory.";
	}

}
