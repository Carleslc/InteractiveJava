package me.carleslc.interactiveJava;

import java.util.Locale;

import com.fathzer.soft.javaluator.DoubleEvaluator;

import me.carleslc.interactiveJava.console.Console;
import me.carleslc.utils.StringUtils;

public abstract class InteractiveJava {
	
	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		new InteractiveJavaFrame();
	}
	
	public static String process(Console console, String line, boolean ignoreVariable) {
		String replaced = StringUtils.replaceVariables(line, console.getVariables());
		line = StringUtils.formatLiterals(line.trim());
		String[] args = line.split("\\s");
		line = line.replace(StringUtils.LITERAL_REPLACEMENT, " ");
		args = StringUtils.undoLiterals(args);
		try {
			// TODO implement own evaluator for >, <, >=, <=, ==, !, NOT, &&, AND, ||, OR
			String evaluated = new DoubleEvaluator().evaluate(replaced).toString();
			console.printAsConsole(evaluated);
			return evaluated;
		} catch (Exception e) {
			if (args.length > 2 && args[1].equals("=")) {
				String[] argsReplaced = replaced.split("\\s*=\\s*", 2);
				boolean printing = console.isEnablePrinting();
				console.enablePrinting(false);
				String value = process(console, argsReplaced.length > 1 ? argsReplaced[1] : args[2], true);
				console.enablePrinting(printing);
				if (value == null)
					value = console.getLastCommand();
				value = value.toString().replace("\"", "");
				console.addVariable(args[0], value);
				console.printAsConsole(value);
				return value;
			}
			else if (!ignoreVariable && console.existsVariable(args[0])) {
				String value = console.getVariable(args[0]);
				console.printAsConsole(value);
				return value;
			}
			else
				return console.executeCommand(line, args);
		}
	}
	
}
