package me.mrfcker.interactiveJava;

import com.fathzer.soft.javaluator.DoubleEvaluator;

import me.mrfcker.interactiveJava.console.Console;
import me.mrfcker.utils.StringUtils;

public abstract class InteractiveJava {
	
	public static void main(String[] args) {
		new InteractiveJavaFrame();
		// TODO Save (State) and Load (State)
	}
	
	public static Object process(Console console, String line) {
		String replaced = StringUtils.replaceVariables(line.toLowerCase(), console.getVariables());
		replaced = replaced.replace("\"", "");
		try {
			// TODO implements own evaluator for >, <, >=, <=, ==, !, NOT, &&, AND, ||, OR,
			// +, -, *, /, % and variables set at console
			Object evaluated = new DoubleEvaluator().evaluate(replaced);
			console.printAsConsole(evaluated.toString());
			return evaluated;
		} catch (Exception e) {
			line = StringUtils.formatLiterals(line.trim());
			String[] args = line.split("\\s", 3);
			line = line.replace("§", " ");
			args = StringUtils.undoLiterals(args);
			if (args.length > 2 && args[1].equals("=")) {
				String[] argsReplaced = replaced.split("\\s*=\\s*", 2);
				boolean printing = console.isEnablePrinting();
				console.enablePrinting(false);
				Object value = process(console, argsReplaced.length > 1 ? argsReplaced[1] : args[2]);
				console.enablePrinting(printing);
				if (value == null)
					value = console.getLastCommand();
				console.addVariable(args[0], value);
				console.printAsConsole(value.toString());
				return value;
			}
			else if (console.existsVariable(args[0])) {
				Object value = console.getVariable(args[0]);
				console.printAsConsole(value.toString());
				return value;
			}
			else
				return console.executeCommand(line, args);
		}
	}
	
}
