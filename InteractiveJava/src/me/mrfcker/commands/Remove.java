package me.mrfcker.commands;

import java.io.File;

import javax.swing.JOptionPane;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;
import me.mrfcker.utils.StringUtils;

public class Remove implements Command {

	@Override
	public String execute(Console console, String[] args) {
		if (args.length > 1) {
			String[] rawArgs = args;
			args = StringUtils.remove(args, "-f");
			boolean force = rawArgs.length != args.length;
			String arg = StringUtils.concat(args, 1);
			if (console.existsVariable(arg)) {
				String oldValue = console.removeVariable(arg);
				if (oldValue == null)
					console.printError(arg + " is a system variable.");
				return oldValue;
			}
			else {
				String path = console.getVariable("PWD").toString() + arg;
				File f = new File(path);
				if (f.exists()) {
					if (force) delete(console, f);
					else deleteWithWarning(console, f);

					console.handleInput(false);
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
	
	private void deleteWithWarning(Console console, File f) {
		boolean isDir = f.isDirectory();
		String title = "Delete " + (isDir ? "directory " : "file ");
		String message = isDir ?
				"Directory " + f.getName() + " along wiht its contents will be deleted permanently, are you sure?"
				: "File " + f.getName() + " will be deleted permanently, are you sure?";
		int opt = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (opt == JOptionPane.YES_OPTION) delete(console, f);
	}
	
	private void deleteDirRecursive(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory())
				deleteDirRecursive(file);
			file.delete();
		}
	}
	
	private void delete(Console console, File f) {
		if (f.isDirectory()) deleteDirRecursive(f);
		f.delete();
		console.printAsHelp(f.getName() + " has been deleted.");
	}

	@Override
	public String help() {
		return "rm {variable} - Removes a variable.\n"
				+ "rm {file} [-f] - Delete a file of the current directory (PWD).\n"
				+ "\tWith -f bypasses the verification stage.";
	}

}
