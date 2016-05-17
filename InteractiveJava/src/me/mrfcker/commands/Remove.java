package me.mrfcker.commands;

import java.io.File;

import javax.swing.JOptionPane;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;

public class Remove implements Command {

	@Override
	public Object execute(Console console, String[] args) {
		if (args.length > 1) {
			String arg = args[1];
			if (console.existsVariable(arg)) {
				Object oldValue = console.removeVariable(arg);
				if (oldValue == null)
					console.printError(arg + " is a system variable.");
				return oldValue;
			}
			else {
				String path = console.getVariable("PWD").toString() + arg;
				File f = new File(path);
				if (f.exists()) {
					int opt = JOptionPane.showConfirmDialog(null, "File " + arg +
							" will be deleted permanently, are you sure?");
					if (opt == JOptionPane.YES_OPTION) {
						f.delete();
						console.printAsHelp(f.getName() + " has been deleted.");
					}
					console.handleInput(false);
					//}
					return path;
				}
				else
					console.printError(arg + " not found.");
			}
		}
		else
			console.printAsHelp(help());
		return null;
	}

	@Override
	public String help() {
		return "rm {variable} - Removes a variable.\n"
				+ "rm {file} [-f] - Delete a file of the current directory (PWD).\n"
				+ "\tWith -f bypasses the verification stage.";
	}

}
