package me.mrfcker.interactiveJava;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import me.mrfcker.interactiveJava.console.Console;
import me.mrfcker.interactiveJava.console.InteractiveJavaConsole;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

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
		setTitle("Interactive Java - " + console.getVariable("PWD"));

		setVisible(true);
	}

	public Console getConsole() {
		return console;
	}

}
