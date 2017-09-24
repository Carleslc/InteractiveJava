package me.mrfcker.interactiveJava;

import com.fathzer.soft.javaluator.DoubleEvaluator;

import me.mrfcker.interactiveJava.console.Console;
import me.mrfcker.utils.StringUtils;

public abstract class InteractiveJava {
	
	public static void main(String[] args) {
		new InteractiveJavaFrame();
	}
	
	public static Object process(Console console, String line, boolean ignoreVariable) {
		String replaced = StringUtils.replaceVariables(line.toLowerCase(), console.getVariables());
		line = StringUtils.formatLiterals(line.trim());
		String[] args = line.split("\\s");
		line = line.replace(StringUtils.LITERAL_REPLACEMENT, " ");
		args = StringUtils.undoLiterals(args);
		try {
			// TODO implement own evaluator for >, <, >=, <=, ==, !, NOT, &&, AND, ||, OR
			Object evaluated = new DoubleEvaluator().evaluate(replaced);
			console.printAsConsole(evaluated.toString());
			return evaluated;
		} catch (Exception e) {
			if (args.length > 2 && args[1].equals("=")) {
				String[] argsReplaced = replaced.split("\\s*=\\s*", 2);
				boolean printing = console.isEnablePrinting();
				console.enablePrinting(false);
				Object value = process(console, argsReplaced.length > 1 ? argsReplaced[1] : args[2], true);
				console.enablePrinting(printing);
				if (value == null)
					value = console.getLastCommand();
				value = value.toString().replace("\"", "");
				console.addVariable(args[0], value);
				console.printAsConsole(value.toString());
				return value;
			}
			else if (!ignoreVariable && console.existsVariable(args[0])) {
				Object value = console.getVariable(args[0]);
				console.printAsConsole(value.toString());
				return value;
			}
			else
				return console.executeCommand(line, args);
		}
	}
	
}
