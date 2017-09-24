package me.carleslc.utils;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class Test {

	private static final String[] battle = {"Hasting", "Trafalgar", "Waterloo"};
	private static final String[] date = {"1066", "1805", "1812"};
	
	void play() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Battle test. ");
		for (int i = 0; i < battle.length; ++i) {
			System.out.println(" In which year " + battle[i] + ": ");
			String ans = scan.nextLine();
			if (ans.equals(date[i]))
				System.out.print("Good answer !");
			else
				System.out.print("No it was in " + date[i]);
		}
		System.out.println("   Bye");
		scan.close();
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		new TestGUI(t);
	}
	
}

class TestGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 7348477935426514353L;
	
	JTextField tfIn;
	JLabel lblOut;

	private final PipedInputStream inPipe = new PipedInputStream(); 
    private final PipedInputStream outPipe = new PipedInputStream(); 

	PrintWriter inWriter;
	
	TestGUI(Test gb) {
		super("Great Battles test");
	 
	    // 2. set the System.in and System.out streams 
	    System.setIn(inPipe);
	    try {
	    	System.setOut(new PrintStream(new PipedOutputStream(outPipe), true));
	    	inWriter = new PrintWriter(new PipedOutputStream(inPipe), true);
	    } catch(IOException e) {
	    	System.out.println("Error: " + e);
	    	return;
	    }
	    
	    JPanel p = new JPanel(new BorderLayout());
	    lblOut = new JLabel();
	    p.add(lblOut, BorderLayout.NORTH);
	    tfIn = new JTextField();
	    tfIn.addActionListener(this);
	    p.add(tfIn, BorderLayout.CENTER);
	    
	    add(p);
	    
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setSize(500, 100);
		
	    new SwingWorker<Void, String>() {
	         protected Void doInBackground() throws Exception {
	            Scanner s = new Scanner(outPipe);
	            while (s.hasNextLine()) {
	            		 String line = s.nextLine();
		            	 publish(line);
	            }
	            s.close();
	            return null;
	        }
	         @Override protected void process(java.util.List<String> chunks) {
	             for (String line : chunks) lblOut.setText(line);
	         }
	    }.execute(); 

		gb.play();
	}
	
	public void actionPerformed(ActionEvent e) {
		String text = tfIn.getText();
		tfIn.setText("");
		inWriter.println(text); 
	}
	
}

