package me.mrfcker.interactiveJava.console;

class Variable {
	
	private String value;
	private boolean isSystemVariable;
	
	public Variable(String value) {
		this(value, false);
	}
	
	public Variable(String value, boolean isSystemVariable) {
		this.value = value;
		this.isSystemVariable = isSystemVariable;
	}

	public boolean isSystemVariable() {
		return isSystemVariable;
	}
	
	public void setSystemVariable(boolean isSystemVariable) {
		this.isSystemVariable = isSystemVariable;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value.toString();
	}
	
}
