package me.mrfcker.commands;

import java.util.Map.Entry;

import me.mrfcker.exceptions.AlreadyExistsException;
import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;
import me.mrfcker.utils.StringUtils;

public class Alias implements Command {

	@Override
	public Object execute(Console console, String[] args) {
		if (args.length > 2) {
			try {
				Command.addAlias(args[1], args[2]);
				console.printAsHelp("Alias added successfully.");
				return args[2];
			} catch (AlreadyExistsException e) {
				console.printError(e.getMessage());
			} catch (Exception e) {
				console.printError(args[1] + " not found.");
			}
		}
		else if (args.length == 2) {
			String cmd = args[1];
			if (cmd.startsWith("-")) {
				cmd = cmd.substring(1);
				// TODO check for system alias (cannot be removed)
				// TODO print message if alias not exists
				Command oldCommand = alias.remove(StringUtils.formatAsCommand(cmd));
				if (oldCommand != null)
					console.printAsHelp(cmd + " has been removed from " + oldCommand.getName());
				return oldCommand;
			}
			else {
				String res = "";
				for (Entry<String, Command> e : alias.entrySet()) {
					if (e.getKey().equalsIgnoreCase(cmd)) {
						console.printAsHelp(cmd + " is an alias of " + e.getValue().getName());
						res = cmd + "\n";
						break;
					}
					if (e.getValue().getName().equalsIgnoreCase(cmd)) {
						String alias = e.getKey().toLowerCase();
						res += alias + "\n";
						console.printAsHelp(alias);
					}
				}
				if (res.isEmpty()) {
					console.printAsHelp(cmd + " doesn't have any alias.");
					return cmd;
				}
				return res.substring(0, res.length() - 1);
			}
		}
		else
			console.printAsHelp(help());
		return null;
	}

	@Override
	public String help() {
		return "alias {command} {new name} - Creates an alias name for a command.\n"
				+ "alias {command} - Shows all alias of a command.\n"
				+ "alias -{alias} - Remove an existing alias.";
	}

}
