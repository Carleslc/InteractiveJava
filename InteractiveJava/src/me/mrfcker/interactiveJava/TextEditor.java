package me.mrfcker.interactiveJava;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import me.mrfcker.interactiveJava.console.Console;

public class TextEditor extends JFrame {

	private static final long serialVersionUID = -319300260694985726L;
	
	private Console attached;
	
	private JPanel contentPane;
	private JTextArea textArea;
	
	private File file;

	public TextEditor(Console attached, File file) {
		this.attached = attached;
		this.file = file;
		final JFrame instance = this;
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int opt = JOptionPane.showConfirmDialog(null, "Save file?");
                if (opt == JOptionPane.YES_OPTION && file != null)
                	save();
                if (opt != JOptionPane.CANCEL_OPTION)
                	attached.dispose(instance);
            }
        });
		setBounds(0, 0, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setTabSize(2);
		textArea.setFont(new Font("Lucida Sans Unicode", Font.PLAIN, 13));
		scrollPane.setViewportView(textArea);
		setTitle("Interactive Java Text Editor - " + file.getAbsolutePath());
	}
	
	public String read() {
		try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
			StringBuilder sb = new StringBuilder();

			String line = br.readLine();
			while (line != null) {
				sb.append(line).append("\n");
				line = br.readLine();
			}

			return sb.toString();
		} catch (Exception e) {}
		return null;
	}
	
	public void save() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			bw.write(textArea.getText());
		} catch (IOException e) {
			attached.printError("Has been an error saving the file " + file.getName());
			e.printStackTrace();
		}
	}

	public void setText(String text) {
		textArea.setText(text);
	}
}
