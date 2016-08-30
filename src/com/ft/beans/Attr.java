package com.ft.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attr {
	private String name;
	private Map<String, Object> condition;
	private Map<String, List<Map<String, Object>>> result;
	private static Map<String, List<String>> fieldOrder;
	private boolean tooutput;
	public Attr(String name) {
		super();
		this.name = name;
		this.condition = new HashMap<String, Object>();
		result = new HashMap<String, List<Map<String, Object>>>();
		fieldOrder = new HashMap<String, List<String>>();
		tooutput = false;
	}
	public Attr() {
		super();
		result = new HashMap<String, List<Map<String, Object>>>();
		tooutput = false;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Object> getCondition() {
		return condition;
	}
	public void addCondition(String name, Object value) {
		this.condition.put(name, value);
	}
	public Map<String, List<Map<String, Object>>> getResult() {
		return result;
	}
	public static Map<String, List<String>> getFieldOrder() {
		return fieldOrder;
	}
	public boolean isTooutput() {
		return tooutput;
	}
	public void setTooutput(boolean tooutput) {
		this.tooutput = tooutput;
	}
	public void clear(){
		result.clear();
		result = null;
	}
}
