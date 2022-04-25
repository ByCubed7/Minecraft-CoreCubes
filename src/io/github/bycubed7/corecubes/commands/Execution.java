package io.github.bycubed7.corecubes.commands;

public class Execution {
	public static final Execution NONE, ARGUMENTLENGTH, USAGE, NOPERMISSION, REQUIRESPLAYER;
	
	static {
		NONE = create();
		ARGUMENTLENGTH = createFail().setReason("Invalid agrument count");
		USAGE 		   = createFail().setReason("Invalid command usage");
		NOPERMISSION   = createFail().setReason("You do not have permission to use this command.").dontShowUsage();
		REQUIRESPLAYER = createFail().setReason("A player must use this command.").dontShowUsage();
	}
	
	public String reason;
	public boolean printUsage;
	public boolean failed;
	
	public Execution() {
		reason = "No reason provided";
		printUsage = false;
		failed = false;
		
		//data = new HashMap<String, String>();
	}
	
	public static Execution create() {
		Execution exe = new Execution();
		return exe;
	}
	
	public static Execution createFail() {
		Execution exe = create();
		exe.printUsage = true;
		exe.failed = true;
		return exe;
	}
	
	// Chains
	
	public Execution setReason(String _reason) {
		reason = _reason;
		return this;
	}
	
	public Execution doShowUsage() {
		printUsage = true;
		return this;
	}
	
	public Execution dontShowUsage() {
		printUsage = false;
		return this;
	}

	public Execution fail() {
		failed = true;
		return this;
	}
	
	public Execution succeed() {
		failed = false;
		return this;
	}
	
	// data?

//	public Map<String, String> data;
//	
//	public String get(String key) {
//		return data.get(key);
//	}
//	public String put(String key, String value) {
//		return data.put(key, value);
//	}
	
}
