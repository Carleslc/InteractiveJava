package me.mrfcker.commands.games;

import java.awt.Color;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.common.base.Strings;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;
import me.mrfcker.utils.StringUtils;
import me.mrfcker.utils.SwingUtils;

public class Ahorcado implements Command {

	private static final int MAX_LIVES = 6;
	private static final String SPACE_INDICATOR = "@";

	private Console console;

	private JPanel inputPanel;

	private HashSet<Character> caracteres;
	private int vidas;
	private String hid, word;
	private boolean complete;

	@Override
	public Object execute(Console console, String[] args) {
		this.console = console;

		inputPanel = new JPanel();
		JLabel lbl = new JLabel("What is your next try?");
		JTextField txt = new JTextField(10);
		txt.addAncestorListener(new SwingUtils.RequestFocusListener(false));
		inputPanel.add(lbl);
		inputPanel.add(txt);

		caracteres = new HashSet<Character>();
		vidas = MAX_LIVES;
		console.printAsHelp("Buscando palabra...");
		word = getRandomWord().toUpperCase().replace(" ", SPACE_INDICATOR);
		hid = Strings.repeat("_ ", word.length());

		complete = false;
		state();

		while (!complete && vidas > 0 && !hid.replace(" ", "").equals(word)) {
			if (JOptionPane.showOptionDialog(null, inputPanel, "Ahorcado",
					JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					new String[] {"OK"}, "OK") == JOptionPane.CLOSED_OPTION) {
				console.printColored("Juego cancelado, la palabra era ", Color.GRAY, false);
				console.printColored(word, Color.MAGENTA, true);
				return word;
			}
			String answer = txt.getText();
			if (!answer.isEmpty()) nextIteration(answer);
			txt.setText("");
		}

		if (vidas <= 0) {
			console.printColored("Has perdido, la palabra era ", Color.RED, false);
			console.printColored(word, Color.MAGENTA, true);
		}
		else
			console.printColored("Correcto! Has ganado.", Color.GREEN);
		return word;
	}

	@Override
	public String help() {
		return "The Hangman in Spanish. You have " + MAX_LIVES + " lives to find out the hidden word.";
	}

	private void nextIteration(String line) {
		String aux = hid;
		line = line.toUpperCase().replace(" ", SPACE_INDICATOR);
		
		if (line.length() > 1) {
			if (line.equals(word))
				complete = true;
			else
				state();
		}
		
		char c = line.charAt(0);

		if (!complete) {
			if (caracteres.add(c)) {
				hid = refresh(word, hid, c);
				if (hid.equals(aux))
					--vidas;
				state();
			}
			else
				console.printAsHelp("Esa letra ya la has puesto!");
		}
	}

	private void state() {
		console.printAsHelp(hid.replace(SPACE_INDICATOR, " ") + "\nLetras: " + word.length());
		printLife(vidas);
		drawStage(6 - vidas);
	}

	private void printLife(int vidas) {
		Color c = vidas > MAX_LIVES/2 ? Color.GREEN : vidas <= 1 ? Color.RED : Color.ORANGE;
		console.printColored("Vidas: ", Color.GRAY, false);
		console.printColored(Integer.toString(vidas), c);
	}

	private static String refresh(String secretWord, String hid, char letter) {
		char[] newHid = hid.toCharArray();
		for (int i = 0; i < hid.length(); i += 2) {
			if (letter == secretWord.charAt(i/2)) {
				newHid[i] = letter;
			}
		}
		return String.valueOf(newHid);
	}

	static int i = 0;
	
	private static String getRandomWord() {
		String word;
		try {
			String source = StringUtils.source("http://www.palabrasque.com/palabra-aleatoria.php?Submit=Nueva+palabra");
			String[] lines = source.split("\n");
			word = StringUtils.withoutSpecials(lines[148].replaceAll("\\<[^<>]*\\>", "").trim());
		} catch (Exception e) {
			word = "REVISA-TU-CONEXION-A-INTERNET";
			e.printStackTrace();
		}
		return word;
	}

	private void drawStage(int stage) {
		console.printAsHelp(" ╔════╗");
		if (stage > 0) {
			console.printAsHelp(" o    ║");
			if (stage == 1) {
				console.printAsHelp("      ║");
				console.printAsHelp("      ║");
			}
			else if (stage == 2) {
				console.printAsHelp("/     ║");
				console.printAsHelp("      ║");
			}
			else if (stage == 3) {
				console.printAsHelp("/|    ║");
				console.printAsHelp("      ║");
			}
			else if (stage >= 4) {
				console.printAsHelp("/|\\   ║");
				if (stage == 5)
					console.printAsHelp("/     ║");
				else if (stage == 6)
					console.printAsHelp("/ \\   ║");
				else
					console.printAsHelp("      ║");
			}
		}
		else {
			for (int i = 0; i < 3; ++i)
				console.printAsHelp("      ║");
		}
		console.printAsHelp("══════╝");
	}

}
