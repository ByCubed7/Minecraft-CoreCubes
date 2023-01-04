package io.github.bycubed7.corecubes.commands.uses;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class Arg {
	protected String name;
	protected String value;
	
	// Argument can't be a varible AND recurring
	protected boolean isVariable;
	protected boolean isRecurring;
		
	public Arg(String _name, String _value) {
		name = _name.toLowerCase();
		value = _value;
		isVariable = false;
		isRecurring = false;
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - -
	// Override Methods

	// Returns 'all' of the current uses the argument could be
	public List<String> use(Arg argument, Player player) {
		return new ArrayList<String>();
	}
	
	// Whether the current input is valid
	public boolean valid(String current, Arg argument, Player player) {
		return true;
	}
	

	// - - - - - - - - - - - - - - - - - - - - - - - - - -
	// Gets
	
	final public String getName() {
		return name;
	}
	
	final public int getSize() {
		return StringUtils.countMatches(value, " ") + 1;
	}
		
	final public String getValue() {
		return value;
	}
	
	final public boolean isVariable() {
		return isVariable;
	}
	
	final public boolean isRecurring() {
		return isRecurring;
	}

	
	// - - - - - - - - - - - - - - - - - - - - - - - - - -
	// Sets

	final public Arg setVariable(boolean is) {
		isVariable = is;
		return this;
	}

	final public Arg setRecurring(boolean is) {
		isRecurring = is;
		return this;
	}

	final public Arg setValue(String _newValue) {
		value = _newValue;
		return this;
	}
	
}
