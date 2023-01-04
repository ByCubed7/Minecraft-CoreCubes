package io.github.bycubed7.corecubes.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import io.github.bycubed7.corecubes.commands.uses.Arg;

public class ArgBuilder {

	List<Arg> contents;
	
	private ArgBuilder() {
		contents = new ArrayList<Arg>();
	}
	
	public static ArgBuilder create() {
		return new ArgBuilder();
	}
	
	public ArgBuilder add(Arg argument) {
		contents.add(argument);
		return this;
	}
	
	public SortedMap<String, Arg> build() {
		SortedMap<String, Arg> data = new TreeMap<String, Arg>();
		
		for (Arg arguments : contents)
			data.put(arguments.getName(), arguments);
		
		return data;
	}
	
	
}
