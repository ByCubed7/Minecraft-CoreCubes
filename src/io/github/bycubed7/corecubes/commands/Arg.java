package io.github.bycubed7.corecubes.commands;

import org.apache.commons.lang.StringUtils;

public class Arg {
	private String name;
	private int size = 1;
	private String value;
	
	// Argument can't be a varible AND recurring
	private boolean isVariable;
	private boolean recurring;
		
	protected Arg(String _name, int _size, String _value) {
		name = _name;
		size = _size;
		value = _value;
		isVariable = false;
		recurring = false;
	}

	public static Arg create(String _name, String _default, int size) {		
		Arg argument = new Arg(
			_name.toLowerCase(),
			size,
			_default
		);
		
		return argument;
	}

	public static Arg create(String _name, String _default) {
		return create(_name, _default, StringUtils.countMatches(_default, " ") + 1);
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - -
	// Usage

	
	// - - - - - - - - - - - - - - - - - - - - - - - - - -
	// Gets
	
	public String getName() {
		return name;
	}
	
	public int getSize() {
		return size;
	}
		
	public String getValue() {
		return value;
	}

	public void setValue(String _newValue) {
		value = _newValue;
	}
	
	public boolean isVariable() {
		return isVariable;
	}
	
	public boolean isRecurring() {
		return recurring;
	}

	
	// - - - - - - - - - - - - - - - - - - - - - - - - - -
	// Sets

	public Arg setSize(int newSize) {
		size = newSize;
		return this;
	}

	public Arg setVariable(boolean is) {
		isVariable = is;
		return this;
	}

	public Arg setRecurring(boolean is) {
		recurring = is;
		return this;
	}
}
