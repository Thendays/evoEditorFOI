package com.evolaris.editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class JSONObj {
	private String jsonObj;
	private HashMap<String, String> list;
	
	public JSONObj() {
		list = new HashMap<String, String>();
		jsonObj = new String();
		jsonObj = "";
	}
	
	public void put(String name, String value) {
		list.put(name, value);
	}
	
	@Override
	public String toString() {
		jsonObj += "{";
		for (Entry<String, String> entry : list.entrySet()) {
			jsonObj += "\"" + entry.getKey() + "\": " + "\"" + entry.getValue() + "\", ";
		}
		jsonObj = jsonObj.substring(0, jsonObj.lastIndexOf(','));
		jsonObj += "}";
		return jsonObj;
	}
}
