package com.ft.export;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

public class StyleFactory {
	private static final Map<Workbook, Map<String, CellStyle>> styleMap
			= new HashMap<Workbook, Map<String, CellStyle>>();
	
	public static CellStyle getTitleStyle(Workbook book){
		if(styleMap.get(book) == null){
			styleMap.put(book, new HashMap<String, CellStyle>());
		}
		if(styleMap.get(book).get("title") == null){
			CellStyle style = book.createCellStyle();
			style.setAlignment(CellStyle.ALIGN_LEFT);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); 
			style.setAlignment(CellStyle.ALIGN_LEFT);
			Font headerFont = book.createFont();  
			headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			headerFont.setFontName("Times New Roman");
			headerFont.setFontHeightInPoints((short) 8);
			style.setFont(headerFont);
			styleMap.get(book).put("title", style);
			return style;
		}else{
			return styleMap.get(book).get("title");
		}
	}
	public static CellStyle getHeadStyle(Workbook book){
		if(styleMap.get(book) == null){
			styleMap.put(book, new HashMap<String, CellStyle>());
		}
		if(styleMap.get(book).get("head") == null){
			CellStyle style = book.createCellStyle();
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
			style.setAlignment(CellStyle.ALIGN_CENTER);  
			Font headerFont2 = book.createFont();  
			headerFont2.setBoldweight(Font.BOLDWEIGHT_BOLD);  
			headerFont2.setFontName("Times New Roman");  
			headerFont2.setFontHeightInPoints((short) 8);  
			style.setFont(headerFont2);  
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN); 
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN); 
			styleMap.get(book).put("head", style);
			return style;
		}else{
			return styleMap.get(book).get("head");
		}
	}
	public static CellStyle getContentStyle(Workbook book){
		if(styleMap.get(book) == null){
			styleMap.put(book, new HashMap<String, CellStyle>());
		}
		if(styleMap.get(book).get("content") == null){
			CellStyle style = book.createCellStyle();
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
			style.setAlignment(CellStyle.ALIGN_CENTER);  
			Font headerFont2 = book.createFont();  
			headerFont2.setBoldweight(Font.BOLDWEIGHT_BOLD);
			headerFont2.setFontName("Times New Roman");  
			headerFont2.setFontHeightInPoints((short) 8);  
			style.setFont(headerFont2);  
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN); 
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			styleMap.get(book).put("content", style);
			return style;
		}else{
			return styleMap.get(book).get("content");
		}
	}
	public static CellStyle getNumberStyle(Workbook book){
		if(styleMap.get(book) == null){
			styleMap.put(book, new HashMap<String, CellStyle>());
		}
		if(styleMap.get(book).get("number") == null){
			CellStyle style = book.createCellStyle();
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
			style.setAlignment(CellStyle.ALIGN_RIGHT);  
			Font headerFont2 = book.createFont();  
			headerFont2.setBoldweight(Font.BOLDWEIGHT_BOLD);
			headerFont2.setFontName("Times New Roman");  
			headerFont2.setFontHeightInPoints((short) 8);  
			style.setFont(headerFont2);  
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN); 
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			styleMap.get(book).put("number", style);
			return style;
		}else{
			return styleMap.get(book).get("number");
		}
	}
	public static CellStyle getDateStyle(Workbook book){
		if(styleMap.get(book) == null){
			styleMap.put(book, new HashMap<String, CellStyle>());
		}
		if(styleMap.get(book).get("date") == null){
			CellStyle style = book.createCellStyle();
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
			style.setAlignment(CellStyle.ALIGN_CENTER);  
			DataFormat format = book.createDataFormat();
			style.setDataFormat(format.getFormat("yyyy-MM-dd"));
			Font headerFont2 = book.createFont();  
			headerFont2.setBoldweight(Font.BOLDWEIGHT_BOLD);
			headerFont2.setFontName("Times New Roman");  
			headerFont2.setFontHeightInPoints((short) 8);  
			style.setFont(headerFont2);  
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN); 
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			styleMap.get(book).put("date", style);
			return style;
		}else{
			return styleMap.get(book).get("date");
		}
	}
}
