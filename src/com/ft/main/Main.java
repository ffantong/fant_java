package com.ft.main;

import com.ft.conf.ToolException;
import com.ft.db.ConnectionUtil;
import com.ft.db.FindByCondition;
import com.ft.xmlparser.Parser;

/**
 * 
 * Tool 2015-09-09
 * @author fengxw
 *
 */
public class Main {
	/**
	 * 版本信息，小的改动增加0.0.1
	 * 大的改动增加0.1  不断完善中
	 */
	private static final String version = "2.1.2";

	public static void main(String[] args) {
		try {
			System.out.println("Tool Version: " + version);
			new Parser().parserXml();
			new FindByCondition().runQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				System.out.println("Complete...");
				ConnectionUtil.closeConnection();
			} catch (ToolException e) {}
		}
	}
}
