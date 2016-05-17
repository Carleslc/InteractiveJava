package me.mrfcker.interactiveJava.console;

class Variable {
	
	private Object value;
	private boolean isSystemVariable;
	
	public Variable(Object value) {
		this(value, false);
	}
	
	public Variable(Object value, boolean isSystemVariable) {
		this.value = value;
		this.isSystemVariable = isSystemVariable;
	}

	public boolean isSystemVariable() {
		return isSystemVariable;
	}
	
	public void setSystemVariable(boolean isSystemVariable) {
		this.isSystemVariable = isSystemVariable;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String toString() {
		return value.toString();
	}
	
}
