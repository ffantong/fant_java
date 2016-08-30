package com.ft.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ft.conf.ExpTyp;
import com.ft.conf.ToolException;

public class ResultSetFactory {
	private static final String SQL_HEAD = "SELECT ";
	private static final String SQL_BODY = " FROM ";
	private static final String SQL_FOOT = " WHERE ";
	private static PreparedStatement pst;
	private static ResultSet rs;
	public static ResultSet getSimpleResultSet(String fields, String name, 
			String where, String order, Map<String, Object> condition, String sqlStr) throws ToolException {
		StringBuffer sql = new StringBuffer();
		try{
			Connection con = ConnectionUtil.getConnection();
			if(sqlStr == null){
				sql.append(SQL_HEAD);
				sql.append(fields);
				sql.append(SQL_BODY);
				sql.append(name);
				if(where != null && !where.equals("")){
					sql.append(SQL_FOOT);
					sql.append(where);
				}
				if(order != null && !order.equals("")){
					sql.append(" ORDER BY ");
					sql.append(order);
				}
				pst = con.prepareStatement(sql.toString());
			}else{
				pst = con.prepareStatement(sqlStr);
				sql.append(sqlStr);
			}
			String[] params = getParams(sql.toString());
			Object paramValue;
			for(int i = 1; i <= params.length; i++){
				paramValue = condition.get(params[i - 1].toUpperCase());
				if(paramValue instanceof Float){
					pst.setFloat(i, (Float) paramValue);
				}else if(paramValue instanceof Double){
					pst.setDouble(i, (Double) paramValue);
				}else if(paramValue instanceof Short){
					pst.setShort(i, (Short) paramValue);
				}else if(paramValue instanceof Integer){
					pst.setInt(i, (Integer) paramValue);
				}else if(paramValue instanceof Long){
					pst.setLong(i, (Long) paramValue);
				}else if(paramValue instanceof Date){
					pst.setDate(i, new java.sql.Date(((Date) paramValue).getTime()));
				}else{
					pst.setString(i, (String) paramValue);
				}
			}
			ResultSet rs = pst.executeQuery();
			return rs;
		}catch(ToolException e){
			throw e;
		}catch(Exception e){
			System.err.println("SQL ERROR:" + sql);
			throw new ToolException(e, ExpTyp.SQL_EXP);
		}
	}
	private static String regex = "(\\s+(\\w+)|\\w+\\.(\\w+))\\s*=\\s*\\?";
	private static Pattern pattern = Pattern.compile(regex);
	private static String[] getParams(String sql){
		Matcher matcher = pattern.matcher(sql);
		int count = 0;
		for(int i = 0; i < sql.length(); i++){
			if(sql.charAt(i) == '?'){
				count++;
			}
		}
		String[] result = new String[count];
		count = 0;
		while(matcher.find()){
			result[count++] = matcher.group(2) == null ? matcher.group(3) : matcher.group(2);
		}
		return result;
	}
	
	public static void close() throws SQLException{
		if(pst != null){
			pst.close();
		}
		if(rs != null){
			rs.close();
		}
	}
}
