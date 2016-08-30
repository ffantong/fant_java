package com.ft.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ft.beans.Attr;
import com.ft.beans.Sheet;
import com.ft.beans.Table;
import com.ft.conf.Config;
import com.ft.conf.ExpTyp;
import com.ft.conf.ToolException;
import com.ft.export.DateUtil;
import com.ft.export.ExcelTool;

public class FindByCondition {
	
	private Config conf = null;
	public FindByCondition(){
		conf = Config.getInstance();
	}
	public void runQuery() throws ToolException {
		try{
			for (Attr attr : conf.getAttrList()) {
				Map<String, List<Map<String, Object>>> attrMap = attr.getResult();
				for(Sheet sht : conf.getSheetList()){
					for(Table tab : sht.getTable()){
						ResultSet rs = ResultSetFactory.getSimpleResultSet(tab.getField(), tab.getName(),
								tab.getWhere(), tab.getOrder(),attr.getCondition(), tab.getSql());
						List<Map<String, Object>> tabList = new ArrayList<Map<String, Object>>();
						List<String> fields = null;
						if(!Attr.getFieldOrder().containsKey(tab.getName())){
							fields = new ArrayList<String>();
							Attr.getFieldOrder().put(tab.getName(), fields);
						}
						attrMap.put(tab.getName(), tabList);
						fixTable(rs, tabList, fields, tab.getName());
						ResultSetFactory.close();
					}
				}
				attr.setTooutput(true);
				ExcelTool.writeTo();
			}
		}catch(ToolException e){
			throw e;
		}catch(Exception e){
			throw new ToolException(e, ExpTyp.SQL_EXP);
		}
	}
	public void fixTable(ResultSet rs, List<Map<String, Object>> list,
					List<String> fields, String table) throws ToolException{
		boolean head = true;
		try {
			while(rs.next()){
				if(head){
					list.add(fixMap(rs, fields, table));
					head = false;
				}else{
					list.add(fixMap(rs, null, table));
				}
			}
		} catch (Exception e) {
			throw new ToolException(e, ExpTyp.SQL_EXP);
		}
	}
	public Map<String, Object> fixMap(ResultSet rs, List<String> fields, String table) throws ToolException {
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			ResultSetMetaData data = rs.getMetaData();
			int size = data.getColumnCount();
			for(int i = 1; i <= size; i++){
				if(conf.isType_detact()){
					if(conf.contains(data.getColumnName(i).trim())){
						Date dt = DateUtil.parse(rs.getString(i));
						if(dt != null){
							map.put(data.getColumnName(i).trim(), 
									DateUtil.parse(rs.getString(i)));
						}else{
							map.put(data.getColumnName(i).trim(), rs.getString(i));
						}
					}else if(TypeDetact.getTypeDetact().detact(table, data.getColumnName(i).trim()) ==
						TypeDetact.TYPE_STRING){
						map.put(data.getColumnName(i).trim(), rs.getString(i));
					}else{
						map.put(data.getColumnName(i).trim(), rs.getFloat(i));
					}
				}else{
					map.put(data.getColumnName(i).trim(), rs.getString(i));
				}
				if(fields != null){
					fields.add(data.getColumnName(i).trim());
				}
			}
			return map;
		}catch(Exception e){
			throw new ToolException(e, ExpTyp.SQL_EXP);
		}
	}
}
