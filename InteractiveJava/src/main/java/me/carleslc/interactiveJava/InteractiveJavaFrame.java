package me.carleslc.interactiveJava;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import me.carleslc.interactiveJava.console.Console;
import me.carleslc.interactiveJava.console.InteractiveJavaConsole;
import me.carleslc.serialnumber.OS;

public class InteractiveJavaFrame extends JFrame {

	private static final long serialVersionUID = -3262052179876882175L;

	private InteractiveJavaConsole console;

	public InteractiveJavaFrame() {
		setBounds(0, 0, 800, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
				.addComponent(scrollPane_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
				);

		console = new InteractiveJavaConsole(this);
		scrollPane_1.setViewportView(console.getVariableTable());
		scrollPane.setViewportView(console);

		getContentPane().setLayout(groupLayout);
		getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{scrollPane_1, scrollPane, console}));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// TODO: Save and load state (e.g. variables and aliases)
		setTitle("InteractiveJava - " + console.getVariable("PWD"));

		setDefaultStyle();
		center(this);
		
		setVisible(true);
	}

	public Console getConsole() {
		return console;
	}
	
	private static void center(JFrame frame) {
		frame.pack();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		final int frameWidth = frame.getWidth();
		final int frameHeight = frame.getHeight();
		int x = (int) ((rect.getMaxX()) - frameWidth)/2;
		int y = (int) ((rect.getMaxY()) - frameHeight)/2;
		frame.setLocation(x, y);
		int width = (int) Math.min(frameWidth, rect.getMaxX());
		int height = (int) Math.min(frameHeight, rect.getMaxY());
		Dimension size = new Dimension(width, height);
		frame.setMinimumSize(size);
		frame.setPreferredSize(size);
	}
	
	private static final void setDefaultStyle() {
		try {
			// Nimbus L&F style
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			// Base Background
			UIManager.put("control", Color.WHITE);
			// ToolTip Background
			UIManager.put("info", Color.getHSBColor(185/360f, 0.15f, 0.97f));
			// Buttons Background
			UIManager.put("nimbusBase", Color.getHSBColor(200/360f, 0.15f, 0.65f));
			// ComboBox Highlight Background
			UIManager.put("ComboBox:\"ComboBox.listRenderer\"[Selected].background", Color.LIGHT_GRAY);
			// Default colors
			UIManager.getLookAndFeelDefaults().put("nimbusOrange", UIManager.getColor("nimbusBase"));
			UIManager.getLookAndFeelDefaults().put("nimbusGreen", Color.getHSBColor(100/360f, 0.65f, 0.85f));
		} catch (Exception notFoundThenUseDefault) {}
		if (OS.get() == OS.MAC_OS) {
			InputMap im = (InputMap) UIManager.get("TextField.focusInputMap");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.META_DOWN_MASK), DefaultEditorKit.cutAction);
		}
	}

}
