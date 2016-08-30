package com.ft.db.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ft.db.ConnectionUtil;
import com.ft.db.TypeDetact;

public class TypeORACLE extends TypeDetact {

	@Override
	public void detact(String table) throws Exception{
		String[] tables = table.split(",");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DATA_TYPE, COLUMN_NAME, DATA_SCALE FROM USER_TAB_COLUMNS WHERE TABLE_NAME= ?");
		for(int i = 1; i < tables.length; i++){
			sql.append(" OR TABLE_NAME = ?");
		}
		PreparedStatement pst = ConnectionUtil.getConnection().prepareStatement(sql.toString());
		for(int i = 0; i < tables.length; i++){
			table = tables[i];
			table = table.trim().toUpperCase();
			if(table.indexOf(' ') != -1){
				table = table.trim().substring(0, table.indexOf(' '));
			}
			pst.setString(i + 1, table);
		}
		ResultSet rs = pst.executeQuery();
		String type = null;
		try{
			while(rs.next()){
				type = rs.getString(1);
				if((type.toUpperCase().contains("NUMBER")
						||type.toUpperCase().contains("FLOAT"))
						&&rs.getInt(3) != 0){
					typMap.put(rs.getString(2).trim(), TYPE_NUM);
				}else{
					typMap.put(rs.getString(2).trim(), TYPE_STRING);
				}
			}
		}catch (Exception e) {
			throw e;
		}finally{
			if(pst != null){
				pst.close();
			}
			if(rs != null){
				rs.close();
			}
		}
	}
}
