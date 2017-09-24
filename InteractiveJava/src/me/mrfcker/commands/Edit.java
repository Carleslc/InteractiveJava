package me.mrfcker.commands;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.TextEditor;
import me.mrfcker.interactiveJava.console.Console;

import java.io.File;
import java.io.IOException;

public class Edit implements Command {

	@Override
	public String execute(Console console, String[] args) {
		if (args.length > 1) {
			File f = new File(console.getVariable("PWD") + args[1]);
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					console.printError(args[1] + " cannot be opened.");
					e.printStackTrace();
					return null;
				}
			}
			TextEditor editor = new TextEditor(console, f);
			String text = editor.read();
			if (text != null) {
				console.attachFrame(editor);
				editor.setText(text);
				editor.setVisible(true);
				return args[1];
			}
			else {
				console.printError(args[1] + " cannot be opened.");
				editor.dispose();
			}
		}
		else
			console.printAsHelp(help());
		return null;
	}

	@Override
	public String help() {
		return "edit {file} - Opens a file in a text editor.";
	}

}
