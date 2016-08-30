package com.ft.beans;

import java.util.List;

import com.ft.conf.ToolException;

public class Sheet {
	private String name;
	private List<Table> table;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Sheet() {
		super();
	}
	public Sheet(String name, List<Table> tableList) throws ToolException {
		super();
		this.name = name;
		this.table = tableList;
	}
	
	public List<Table> getTable() {
		return table;
	}
}
