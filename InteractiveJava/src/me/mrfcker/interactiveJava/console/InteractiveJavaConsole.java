package me.mrfcker.interactiveJava.console;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.InteractiveJava;
import me.mrfcker.utils.SwingUtils.TableCellListener;

public class InteractiveJavaConsole extends Console {

	private static final long serialVersionUID = -6352919366134708651L;
	
	private DefaultTableModel model;
	private JTable varsTable;

	public InteractiveJavaConsole(JFrame attached) {
		super(attached);
		setModel();
		setVariableTable();
		setSystemVars();
	}
	
	@Override
	public void onNewLine(String lastLine) {
		Object value = InteractiveJava.process(this, lastLine);
		addSystemVariable("ANS", String.valueOf(value), true);
	}

	public void addVariable(String variable, Object value) {
		removeVariable(variable);
		super.addVariable(variable, value);
		model.addRow(new String[] {variable, value.toString()});
	}

	public Object removeVariable(String variable) {
		Object old = super.removeVariable(variable);
		int i = 0;
		boolean found = false;
		while (!found && i < varsTable.getRowCount()) {
			if (varsTable.getValueAt(i, 0).toString().equalsIgnoreCase(variable))
				model.removeRow(i);
			++i;
		}
		return old;
	}
	
	public JTable getVariableTable() {
		return varsTable;
	}
	
	@SuppressWarnings("serial")
	private void setVariableTable() {
		// Variable's table
		varsTable = new JTable();
		varsTable.setShowHorizontalLines(false);
		varsTable.setColumnSelectionAllowed(true);
		varsTable.setCellSelectionEnabled(true);

		new TableCellListener(varsTable, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				replaceVariable((TableCellListener)e.getSource());
			}
		});
		
		varsTable.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {}
		    public void focusLost(FocusEvent focusEvent) {
		    	JTable table = (JTable)focusEvent.getSource();
		    	if (table.isEditing())
		    		table.getCellEditor().stopCellEditing();
		    }
		});

		varsTable.setModel(model);
		varsTable.getColumnModel().getColumn(0).setPreferredWidth(80);
		varsTable.getColumnModel().getColumn(1).setPreferredWidth(130);
		varsTable.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		varsTable.setBounds(500, 0, 185, 412);
		varsTable.getTableHeader().setReorderingAllowed(false);
	}
	
	private void replaceVariable(TableCellListener tcl) {
		if (tcl.getColumn() == 0) { // Changed variable's name
			Object value = super.removeVariable((String)tcl.getOldValue());
			super.addVariable((String)tcl.getNewValue(), value);
		}
		else // Changed variable's value
			super.addVariable((String)varsTable.getValueAt(tcl.getRow(), 0), tcl.getNewValue());
	}

	private void setModel() {
		model = new DefaultTableModel(
				new Vector<>(new Vector<>()),
				new Vector<>(Arrays.asList("Variable", "Value")));
	}

	private void setSystemVars() {
		super.addSystemVariable("PWD", System.getProperty("user.dir") + "\\");
		try {
			Command.addAlias("help", "man");
			Command.addAlias("remove", "rm");
			Command.addAlias("clear", "cls");
			Command.addAlias("change_directory", "cd");
			Command.addAlias("directory", "dir");
			Command.addAlias("directory", "ls");
			Command.addAlias("edit", "cat");
			Command.addAlias("edit", "more");
			Command.addAlias("edit", "less");
		} catch (Exception ignore) {}
	}

}
