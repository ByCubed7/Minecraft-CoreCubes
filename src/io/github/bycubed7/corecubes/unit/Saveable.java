package io.github.bycubed7.corecubes.unit;

import java.util.Map;

public interface Saveable {
	public Map<String, String> getSaveData();
	public void loadSaveData(Map<String, String> data);
}
