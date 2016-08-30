package com.ft.beans;

public class Table {
	private String name;
	private String field;
	private String order;
	private String title;
	private String where;
	private String sql;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Table(String name, String field, String order, String title, String where, String sql) {
		super();
		this.name = name;
		this.field = field;
		if(field == null || field.equals("")){
			field = "*";
		}
		this.order = order;
		this.title = title;
		if(title == null || title.trim().equals("")){
			this.title = name;
		}
		this.where = where;
		this.sql = sql;
	}
	public Table() {
		super();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getSql() {
		return sql;
	}
}
