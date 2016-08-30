package com.ft.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ft.beans.Attr;
import com.ft.beans.Sheet;

public class Config {
	private final static Config instance = new Config();
	private String db_typ;
	private String url;
	private String username;
	private String password;
	private boolean type_detact;
	private List<Sheet> sheetList;
	private List<Attr> attrList;
	private List<String> parse_field;
	private String path;
	private String fileType;
	private Config(){
		attrList = new ArrayList<Attr>();
		sheetList = new ArrayList<Sheet>();
		parse_field = new ArrayList<String>();
		path = "";
	}
	public static Config getInstance(){
		return instance;
	}

	public String getDb_typ() {
		return db_typ;
	}

	public void setDb_typ(String dbTyp) {
		db_typ = dbTyp.toUpperCase();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public List<Attr> getAttrList() {
		return attrList;
	}
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("db_typ:" + db_typ + '\n');
		buf.append("url:" + url + '\n');
		buf.append("username:" + username + '\n');
		buf.append("password:" + password + '\n');
		buf.append("attr" + '\n');
		for(Attr attr : attrList){
			buf.append("Attr: name = " + attr.getName() + ";loanNo = " + attr.getCondition() + '\n');
		}
		buf.append("sheet" + '\n');
		for(Sheet sheet : sheetList){
			buf.append("sheet: name = " + sheet.getName() + ";table = " + sheet.getTable() + '\n');
		}
		return buf.toString();
	}
	public List<Sheet> getSheetList() {
		return sheetList;
	}
	public boolean isType_detact() {
		return type_detact;
	}
	public void setType_detact(boolean typeDetact) {
		type_detact = typeDetact;
	}
	public boolean contains(String field){
		for(String str : parse_field){
			if(str.contains("*")){
				str = str.replaceAll("\\*", ".*");
			}
			Pattern pattern = Pattern.compile(str);
			Matcher matcher = pattern.matcher(field);
			if(matcher.find()){
				return true;
			}
		}
		return false;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<String> getParse_field() {
		return parse_field;
	}
	public boolean checkConnection(){
		if(db_typ == null || db_typ.equals("")){
			return false;
		}
		if(url == null || url.equals("")){
			return false;
		}
		if(username == null || username.equals("")){
			return false;
		}
		if(password == null || password.equals("")){
			return false;
		}
		return true;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
}
