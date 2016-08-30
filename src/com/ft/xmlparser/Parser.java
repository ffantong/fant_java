package com.ft.xmlparser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.ft.beans.Attr;
import com.ft.beans.Sheet;
import com.ft.beans.Table;
import com.ft.conf.Config;
import com.ft.conf.ExpTyp;
import com.ft.conf.ToolException;

public class Parser {
	
	private Config conf = null;
	private SAXBuilder builder= null; 
	private String fileName = "conf.xml";
	private Namespace namespace;
	
	public Parser(){
		builder=new SAXBuilder(false);
		conf = Config.getInstance();
	}
	public void parserXml() throws ToolException {
		parserXml(fileName);
	}

	public void parserXml(String fileName) throws ToolException{
		Document document = null;
		try {
			document = builder.build(fileName);
		} catch (JDOMException e) {
			throw new ToolException(e, ExpTyp.CONF_EXP);
		} catch (IOException e) {
			throw new ToolException(e, ExpTyp.CONF_EXP);
		}   
		Element root=document.getRootElement();
		namespace = root.getNamespace();
		getConnection(root.getChild("connection", namespace));
		getCondition(root.getChild("condition", namespace));
		getResult(root.getChild("result", namespace));
		getParseField(root.getChild("parse_field", namespace));
	}
	public void getConnection(Element connection) throws ToolException{
		conf.setDb_typ(connection.getChild("db_typ", namespace).getValue().trim());
		conf.setUrl(connection.getChild("url", namespace).getValue().trim());
		conf.setUsername(connection.getChild("username", namespace).getValue().trim());
		conf.setPassword(connection.getChild("password", namespace).getValue().trim());
		if(!conf.checkConnection()){
			throw new ToolException(new Exception(), ExpTyp.FILED_EXP);
		}
		boolean typ_detact = "Y".equals(connection.getChild("type_detact", namespace).getValue().toUpperCase().trim())?true:false;
		conf.setType_detact(typ_detact);
		if(connection.getChild("output_path", namespace) != null){
			String path = connection.getChild("output_path", namespace).getValue();
			if(path != null){
				conf.setPath(path);
			}else{
				conf.setPath("");
			}
		}else{
			conf.setPath("");
		}
		if(connection.getChild("file_type", namespace) != null){
			String type = connection.getChild("file_type", namespace).getValue();
			if(type != null){
				conf.setFileType(type);
			}else{
				conf.setFileType("xls");
			}
		}else{
			conf.setFileType("xls");
		}
	}
	@SuppressWarnings("unchecked")
	public void getCondition(Element condition) throws ToolException{
		List<Element> list = condition.getChildren("attr", namespace);
		for(Element attr : list){
			Attr att = new Attr(attr.getAttributeValue("file_name", namespace) == null ||
					attr.getAttributeValue("file_name", namespace).trim().equals("") ? null :
						attr.getAttributeValue("file_name", namespace).trim());
			List<Element> properties = attr.getChildren("property", namespace);
			String attrValue;
			String attrType;
			String name = null;
			for(Element property : properties){
				attrValue = property.getAttributeValue("value");
				if(name == null){
					name = attrValue;
				}
				attrType = property.getAttribute("type") == null ? "String" :
						property.getAttribute("type").getValue();
				att.addCondition(property.getAttribute("name").getValue().toUpperCase(), changeToType(attrType, attrValue));
			}
			if(att.getName() == null || att.getName().equals("")){
				att.setName(name);
			}
			conf.getAttrList().add(att);
		}
	}
	
	private Object changeToType(String type, String val) {
		if (type.equalsIgnoreCase("java.lang.Integer")
				|| type.equalsIgnoreCase("int")) {
			return Integer.parseInt(val);
		}
		if (type.equalsIgnoreCase("java.lang.Short")
				|| type.equalsIgnoreCase("short")) {
			return Short.parseShort(val);
		}
		if (type.equalsIgnoreCase("java.lang.Long")
				|| type.equalsIgnoreCase("long")) {
			return Long.parseLong(val);
		}
		if (type.equalsIgnoreCase("java.lang.Float")
				|| type.equalsIgnoreCase("float")) {
			return Float.parseFloat(val);
		}
		if (type.equalsIgnoreCase("java.lang.Double")
				|| type.equalsIgnoreCase("double")) {
			return Double.parseDouble(val);
		}
		if (type.equalsIgnoreCase("java.util.Date")
				|| type.equalsIgnoreCase("Date")) {
			SimpleDateFormat sdf = new SimpleDateFormat(val.split(":")[0]);
			try {
				return sdf.parse(val.split(":")[1]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return val;
	}
	
	@SuppressWarnings("unchecked")
	public void getResult(Element result) throws ToolException{
		List<Element> list = result.getChildren("sheet", namespace);
		int num = 0;
		for(Element attr : list){
			num++;
			conf.getSheetList().add(new Sheet(attr.getAttributeValue("sheet_name") == null ||
					attr.getAttributeValue("sheet_name").trim().equals("") ? "Sheet" + num:
						attr.getAttributeValue("sheet_name").trim()
					, parseTable(attr.getChildren("table", namespace))));
		}
	}
	private List<Table> parseTable(List<Element> tableList) throws ToolException{
		List<Table> table = new ArrayList<Table>();
		for(Element tab : tableList){
			try{
				String sql = tab.getAttributeValue("use_sql") != null && tab.getAttributeValue("use_sql").trim().equals("true")?
						tab.getChild("sql", namespace).getValue().trim() : null;
				table.add(new Table(tab.getAttributeValue("name").trim(), 
							sql == null ? tab.getChild("field", namespace) == null ? "*" : tab.getChild("field", namespace).getValue().trim() : null, 
							sql == null ? tab.getChild("order", namespace) == null ? "" : tab.getChild("order", namespace).getValue().trim() : null,
							tab.getAttributeValue("title"),
							parseWhere(tab.getChild("where", namespace)),
							sql));
			}catch(Exception e){
				throw new ToolException(e, ExpTyp.SHEET_EXP);
			}
		}
		return table;
	}
	private String parseWhere(Element where){
		if(where == null){
			return null;
		}
		String str = where.getText();
		if(str == null || str.trim().equals("")){
			return null;
		}
		if(where.getAttribute("auto") == null || (
				!where.getAttribute("auto").getValue().equals("AND") && !where.getAttribute("auto").getValue().equals("OR"))){
			return str;
		}
		StringBuilder builder = new StringBuilder();
		String[] strs = str.split(",");
		String link = "AND";
		if(where.getAttribute("auto").getValue().equals("OR")){
			link = "OR";
		}
		for(int i = 0; i < strs.length; i++){
			builder.append(strs[i]);
			builder.append(" = ?");
			if(i != (strs.length - 1)){
				builder.append(link);
			}
		}
		return builder.toString();
	}
	@SuppressWarnings("unchecked")
	public void getParseField(Element parseField){
		List<Element> list = parseField.getChildren("date", parseField.getNamespace());
		if(list == null){
			return;
		}
		for(Element date : list){
			if(date.getValue() != null && !date.getValue().trim().equals("")){
				conf.getParse_field().add(date.getValue().trim());
			}
		}
	}
}
