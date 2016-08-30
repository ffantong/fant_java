package com.ft.db;

import java.util.HashMap;
import java.util.Map;

import com.ft.conf.Config;

public abstract class TypeDetact {
	public static final int TYPE_STRING = 1;
	public static final int TYPE_NUM = 2;
	protected Map<String, Integer> typMap;
	private static TypeDetact typeDetact = null;
	
	public static TypeDetact getTypeDetact() throws Exception{
		Config conf = Config.getInstance();
		if(typeDetact == null){
			StringBuffer name = new StringBuffer("com.ft.db.impl.Type");
			name.append(conf.getDb_typ());
			Class<?> c2=Class.forName(name.toString());
			typeDetact = (TypeDetact) c2.newInstance();
		}
		return typeDetact;
	}
	
	public abstract void detact(String table) throws Exception;
	
	public int detact(String table, String field) throws Exception{
		if(typMap == null){
			typMap = new HashMap<String, Integer>();
		}
		if(!typMap.containsKey(field)){
			detact(table);
		}
		Integer typ = TYPE_STRING;
		if((typ = typMap.get(field)) == null){
			System.err.println("没有检测到字段类型，按默认处理！");
			typ = TYPE_STRING;
		}
		return typ;
	}
}
