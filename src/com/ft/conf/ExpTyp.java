package com.ft.conf;

public enum ExpTyp {
	CONN_EXP("无法获得数据库连接，请检查数据库配置！"),
	SQL_EXP("执行SQL异常，请检查SQL配置！"),
	TYP_EXP("数据库类型错误或不支持的数据库类型，请检查SQL配置！"),
	FILE_EXP("输出文件异常，请检查文件路径，名称配置或文件正在使用中！"),
	CONF_EXP("读取配置文件异常，文件格式错误或不存在！"),
	LOAN_EXP("借据号属性不能为空！"),
	SHEET_EXP("SHEET表配置错误，请检查！"),
	FILED_EXP("数据库配置字段不能为空，请检查！");
	private final String ExpDec;
	ExpTyp(String ExpDec){
		this.ExpDec = ExpDec;
	}
	@Override
	public String toString(){
		return ExpDec;
	}
}
