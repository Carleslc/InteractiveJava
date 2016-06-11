package me.mrfcker.interactiveJava.console;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import me.mrfcker.interactiveJava.Command;

public abstract class Console extends JTextPane implements Serializable {
	
	private static final long serialVersionUID = -7443925490119621526L;
	
	private final PipedInputStream inPipe; 
	private PrintWriter inWriter;
	
	private ArrayList<JFrame> attachedFrames;
	private DocumentFilter filter;
	
	private HashMap<String, Variable> vars;
	private boolean isConsoleMessage, canPrint, lastCommandWasError, handleInput;

	private String prefix, lastCommand;

	public Console(String prefix, JFrame attached) {
		attachedFrames = new ArrayList<JFrame>();
		attachedFrames.add(attached);
		isConsoleMessage = false;
		vars = new HashMap<>();
		setFont(new Font("Lucida Console", Font.PLAIN, 13));
		setPrefix(prefix);
		setFilter(new ConsoleFilter());
		enablePrinting(true);
		handleInput(false);
		
		System.setIn(inPipe = new PipedInputStream());
		try {
			inWriter = new PrintWriter(new PipedOutputStream(inPipe), true);
		} catch(IOException e) {
			printError("Internal Error.");
			e.printStackTrace();
			return;
		}
	}
	
	public Console(JFrame attached) {
		this("", attached);
	}
	
	public Object executeCommand(String command, String[] args) {
		try {
			lastCommand = command;
			lastCommandWasError = false;
			return Command.get(args[0]).execute(this, args);
		} catch (ClassNotFoundException e) {
			printError("Command " + args[0] + " not found.");
		} catch (Exception e) {
			printError("Internal Error.");
			e.printStackTrace();
		}
		lastCommandWasError = true;
		return null;
	}
	
	public Object getVariable(String variable) {
		if (variable != null)
			variable = variable.toLowerCase();
		return vars.get(variable);
	}
	
	public void addVariable(String variable, Object value) {
		if (variable != null)
			variable = variable.toLowerCase();
		if (canReplaceVariable(variable, false))
			vars.put(variable, new Variable(value));
	}
	
	public void addSystemVariable(String variable, Object value) {
		addSystemVariable(variable, value, false);
	}
	
	public void addSystemVariable(String variable, Object value, boolean asConsole) {
		if (variable != null)
			variable = variable.toLowerCase();
		if (canReplaceVariable(variable, asConsole))
			vars.put(variable, new SystemVariable(value));
	}

	public Object removeVariable(String variable) {
		if (variable != null)
			variable = variable.toLowerCase();
		if (canReplaceVariable(variable, false))
			return vars.remove(variable);
		return null;
	}
	
	private boolean canReplaceVariable(String variable, boolean asConsole) {
		if (asConsole)
			return true;
		if (variable != null)
			variable = variable.toLowerCase();
		else
			return false;
		Variable old = vars.get(variable);
		return old == null || !old.isSystemVariable();
	}
	
	public boolean existsVariable(String variable) {
		if (variable != null)
			variable = variable.toLowerCase();
		return vars.containsKey(variable.toLowerCase());
	}
	
	public Map<String, Object> getVariables() {
		return new HashMap<>(this.vars);
	}
	
	public void printError(String msg) {
		printColored(msg, Color.RED);
	}

	public void printAsConsole(String msg) {
		printColored(msg, Color.BLUE);
	}
	
	public void printAsHelp(String msg) {
		printColored(msg, Color.GRAY);
	}

	public void printColored(String msg, Color color) {
		printColored(msg, color, true);
	}
	
	public void printColored(String msg, Color color, boolean newLine) {
		if (canPrint) {
			StyleContext sc = StyleContext.getDefaultStyleContext();
			AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
	
			aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
	
			setCharacterAttributes(aset, false);
			isConsoleMessage = true;
			replaceSelection(msg + (newLine ? "\n" : ""));
		}
	}
	
	public abstract void onNewLine(String lastLine);
	
	public void enablePrinting(boolean enable) {
		canPrint = enable;
	}
	
	public boolean isEnablePrinting() {
		return canPrint;
	}
	
	public void enableFilter(boolean enable) {
		((AbstractDocument)getDocument()).setDocumentFilter(enable ? filter : null);
	}
	
	public boolean isEnableFilter() {
		return ((AbstractDocument)getDocument()).getDocumentFilter() != null;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public JFrame getRootFrame() {
		return attachedFrames.get(0);
	}
	
	public ArrayList<JFrame> getAttachedFrames() {
		return attachedFrames;
	}
	
	public int attachFrame(JFrame frame) {
		attachedFrames.add(frame);
		return attachedFrames.size() - 1;
	}
	
	public void dispose(JFrame frame) {
		dispose(attachedFrames.indexOf(frame));
	}
	
	public void dispose(int attachedIndex) {
		if (attachedIndex >= 0 && attachedIndex < attachedFrames.size()) {
			attachedFrames.get(attachedIndex).dispose();
			attachedFrames.remove(attachedIndex);
		}
	}
	
	public void clear() {
		enableFilter(false);
		setText("");
		setFilter(new ConsoleFilter());
	}
	
	public boolean lastCommandWasError() {
		return lastCommandWasError;
	}

	public String getLastCommand() {
		return lastCommand;
	}
	
	protected void setFilter(DocumentFilter filter) {
		this.filter = filter;
		enableFilter(true);
	}
	
	private void insertPrefix() {
		printColored(prefix, Color.BLACK, false);
	}
	
	private boolean valuable(String s) {
		return s != null && !s.isEmpty() && !s.matches("\\s*");
	}
	
	public boolean isInputHandled() {
		return handleInput;
	}

	public void handleInput(boolean handleInput) {
		this.handleInput = handleInput;
	}

	private class ConsoleFilter extends DocumentFilter {

		private int currentLineOffset, startOffset, endOfLine;
		
		public ConsoleFilter() {
			startOffset = getDocument().getStartPosition().getOffset();
			insertPrefix();
			currentLineOffset = startOffset + prefix.length();
			setCaretPosition(currentLineOffset);
		}
		
		public void insertString(final FilterBypass fb, int offset, String string, AttributeSet attr)
				throws BadLocationException {
			if (!isConsoleMessage) {
				attr = StyleContext.getDefaultStyleContext()
						.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
				setCharacterAttributes(attr, false);
			}
			if (string.endsWith("\n")) {
				String line = getDocument().getText(currentLineOffset, endOfLine - currentLineOffset);
				boolean event = false;
				if (!isConsoleMessage) {
					if (valuable(line)) {
						fb.insertString(endOfLine, string, attr);
						if (isInputHandled())
							inWriter.println(line + "\n");
						endOfLine += string.length();
						event = true;
					}
				}
				else {
					insertPrefix();
					fb.insertString(endOfLine, string, attr);
					endOfLine += string.length();
				}
				currentLineOffset = getDocument().getLength();
				if (event)
					onNewLine(line);
			}
			else {
				fb.insertString(offset, string, attr);
				endOfLine += string.length();
			}
			isConsoleMessage = false;
		}

		public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
			if (isEditable(fb.getDocument(), offset, -length)) {
				fb.remove(offset, length);
				endOfLine -= length;
			}
			else {
				Toolkit.getDefaultToolkit().beep();
				setCaretPosition(endOfLine);
			}
		}

		public void replace(final FilterBypass fb, int offset, final int length, String text, AttributeSet attrs)
				throws BadLocationException {
			if (isEditable(fb.getDocument(), offset, length)) {
				if (!isConsoleMessage) {
					attrs = StyleContext.getDefaultStyleContext()
							.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
					setCharacterAttributes(attrs, false);
				}
				if (text.endsWith("\n")) {
					String line = getDocument().getText(currentLineOffset, endOfLine - currentLineOffset);
					boolean event = false;
					if (!isConsoleMessage) {
						if (valuable(line)) {
							fb.replace(endOfLine, length, text, attrs);
							if (isInputHandled())
								inWriter.println(line + "\n");
							endOfLine += text.length();
							event = true;
						}
					}
					else {
						insertPrefix();
						fb.replace(endOfLine, length, text, attrs);
						endOfLine += text.length();
					}
					currentLineOffset = getDocument().getLength();
					if (event)
						onNewLine(line);
				}
				else {
					fb.replace(offset, length, text, attrs);
					endOfLine += text.length();
				}
			}
			else {
				setCaretPosition(endOfLine);
				replace(fb, endOfLine, length, text, attrs);
			}
			isConsoleMessage = false;
		}
		
		private boolean isEditable(Document doc, int offset, int length) throws BadLocationException {
			return getCaretPosition() + length >= currentLineOffset;
		}
	}
	
}
