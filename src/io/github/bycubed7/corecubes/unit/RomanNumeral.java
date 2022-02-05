package io.github.bycubed7.corecubes.unit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class RomanNumeral {

	private int value;
	private String numeral;

	private static TreeMap<Integer, String> lookup = new TreeMap<Integer, String>();
	// Order is important here \/
	private static LinkedHashMap<String, Integer> lookupReverse = new LinkedHashMap<String, Integer>();

	static {
		lookup.put(1000, "M");
		lookup.put(900, "CM");
		lookup.put(500, "D");
		lookup.put(400, "CD");
		lookup.put(100, "C");
		lookup.put(90, "XC");
		lookup.put(50, "L");
		lookup.put(40, "XL");
		lookup.put(10, "X");
		lookup.put(9, "IX");
		lookup.put(5, "V");
		lookup.put(4, "IV");
		lookup.put(1, "I");

		// Two letters are a prioty over singluar
		lookupReverse.put("CM", 900);
		lookupReverse.put("CD", 400);
		lookupReverse.put("XC", 90);
		lookupReverse.put("XL", 40);
		lookupReverse.put("IX", 9);
		lookupReverse.put("IV", 4);

		lookupReverse.put("M", 1000);
		lookupReverse.put("D", 500);
		lookupReverse.put("C", 100);
		lookupReverse.put("L", 50);
		lookupReverse.put("X", 10);
		lookupReverse.put("V", 5);
		lookupReverse.put("I", 1);
	}

	// Cons

	public RomanNumeral(int newValue) {
		value = newValue;
		numeral = intToNumeral(value);
	}

	public RomanNumeral(String newNumeral) {
		value = numeralToInt(newNumeral);
		numeral = newNumeral;
	}

	// Get

	public String toString() {
		return numeral;
	}

	public int toInt() {
		return value;
	}

	// Set

	public void set(int newValue) {
		value = newValue;
		numeral = intToNumeral(value);
	}

	public void set(String newNumeral) {
		value = numeralToInt(newNumeral);
		numeral = newNumeral;
	}

	// Static

	public final static String intToNumeral(int value) {
		int l = lookup.floorKey(value);
		if (value == l)
			return lookup.get(value);
		return lookup.get(l) + intToNumeral(value - l);
	}

	public final static int numeralToInt(String numeral) {
		if (numeral.length() == 0)
			return 0;

		String left = numeral;

		// Split numeral into segments
		ArrayList<String> segments = new ArrayList<String>();

		Integer inter = 0;
		for (String key : lookupReverse.keySet()) {
			if (!left.contains(key))
				continue;

			int count = (left.length() - left.replaceAll(key, "").length()) / key.length();
			// Add to segments

			for (int j = 0; j < count; j++) {
				segments.add(key);
				inter += lookupReverse.get(key);
			}

			left = left.replaceAll(key, "");
		}

		return inter;
	}

}