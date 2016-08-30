package com.ft.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;

import com.ft.conf.Config;
import com.ft.conf.ExpTyp;
import com.ft.conf.ToolException;

public class ConnectionUtil {
	private static Connection connection = null;
	
	public static void closeConnection() throws ToolException{
		try {
			connection.close();
		} catch (Exception e) {
			connection = null;
			throw new ToolException(e, ExpTyp.CONN_EXP);
		}
	}
	
	public static Connection getConnection() throws ToolException {
		Config conf = Config.getInstance();
		try {
			Field field = DbConst.class.getDeclaredField(conf.getDb_typ());
			Class.forName((String) field.get(null));
			connection = DriverManager.getConnection(
					conf.getUrl(), 
					conf.getUsername(), 
					conf.getPassword());
		} catch (Exception e) {
			connection = null;
			throw new ToolException(e, ExpTyp.CONN_EXP);
		}
		return connection;
	}
}
