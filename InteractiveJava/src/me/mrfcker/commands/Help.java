package me.mrfcker.commands;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Strings;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;
import me.mrfcker.utils.ReflectionClassUtils;

public class Help implements Command {
	
	@Override
	public String help() {
		return "help - Shows all available commands.\n"
				+ "help {command} - Show information about a command.";
	}

	@Override
	public String execute(Console console, String[] args) {
		if (args.length > 1) {
			try {
				String help = Command.get(args[1]).help();
				console.printAsHelp(help);
				return help;
			} catch (Exception e) {
				console.printError(args[1] + " not found.");
			}
		}
		else {
			String intro = "Available commands\n";
			String line = Strings.repeat("-", intro.length());
			StringBuilder res = new StringBuilder(intro + line + "\n");
			List<String> classNames = new LinkedList<>();
			for (Class<?> clazz : ReflectionClassUtils.getAllClasses("me.mrfcker.commands"))
				classNames.add(clazz.getSimpleName());
			Collections.sort(classNames);
			for (String name : classNames)
				res.append(name + "\n");
			res.append(line + "\n");
			res.append("Use help {command} to see more information of a command.");
			String resString = res.toString();
			console.printAsHelp(resString);
			return resString;
		}
		return null;
	}
	
}
