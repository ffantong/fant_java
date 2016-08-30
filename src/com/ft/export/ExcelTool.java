package com.ft.export;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.ft.beans.Attr;
import com.ft.beans.Table;
import com.ft.conf.Config;
import com.ft.conf.ExpTyp;
import com.ft.conf.ToolException;

public class ExcelTool {
	private final Config conf = Config.getInstance();
	private final static ExcelTool excelTool = new ExcelTool();
	private final String FILE_EXT;
	private final String VERSION;
	private ExcelTool(){
		if(conf.getFileType() != null && conf.getFileType().toLowerCase().contains("xlsx")){
			FILE_EXT = ".xlsx";
			VERSION = "org.apache.poi.xssf.usermodel.XSSFWorkbook";
		}else{
			FILE_EXT = ".xls";
			VERSION = "org.apache.poi.hssf.usermodel.HSSFWorkbook";
		}
	}
	public static void writeTo() throws ToolException{
		excelTool.write();
	}
	
	public void write() throws ToolException {
		try {
			for (Attr attr : conf.getAttrList()) {
				if(!attr.isTooutput()){
					continue;
				}
				Workbook book = (Workbook) Class.forName(VERSION).newInstance();
				File file = null;
				if(conf.getPath() != null && !conf.getPath().equals("")){
					file = new File(conf.getPath(), attr.getName() + FILE_EXT);
				}else{
					file = new File(attr.getName() + FILE_EXT);
				}
				FileOutputStream out = null;
				try{
					out = new FileOutputStream(file);
				}catch(Exception e){
					System.out.println("输出目录配置错误，文件输出到当前目录！");
					file = new File(attr.getName() + FILE_EXT);
					out = new FileOutputStream(file);
				}
				System.out.println("Export File:" + file.getAbsolutePath());
				for (com.ft.beans.Sheet sht : conf.getSheetList()) {
					int rowId = 0;
					Sheet sheet = book.createSheet(sht.getName());
					sheet.setDefaultColumnWidth(13);
					for (Table tab : sht.getTable()) {
						System.out.println("Export table:" + tab.getName());
						writeTitle(rowId++, sheet, tab.getTitle(), book, Attr.getFieldOrder().get(tab.getName()).size()-1);
						writeTable(rowId, attr.getResult().get(tab.getName()),
								sheet, Attr.getFieldOrder().get(tab.getName()), book);
						rowId = rowId + attr.getResult().get(tab.getName()).size() + 2;
					}
				}
				book.write(out);
				out.close();
				book.close();
				attr.setTooutput(false);
				attr.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ToolException(e, ExpTyp.FILE_EXP);
		}
	}
	
	public void writeTable(int rowId, List<Map<String, Object>> list,
			Sheet sheet, List<String> fields, Workbook book){
		int currentRow = rowId;
		for(int i = 1; i <= list.size(); i++){
			if(i == 1){
				writeHead(fields, sheet.createRow(currentRow++), book);
			}
			writeOne(list.get(i - 1), sheet.createRow(currentRow++), fields, book);
		}
		writeNull(currentRow++, sheet);
	}
	public void writeOne(Map<String, Object> map, Row row, 
				List<String> fields, Workbook book){
		int celId = 0;
		for(String key : fields){
			Cell cell2 = row.createCell(celId++);
			Object val = map.get(key) == null? "" : map.get(key);
			if(conf.isType_detact()){
				if(val instanceof Date){
					cell2.setCellType(Cell.CELL_TYPE_STRING);
					cell2.setCellValue((Date)val);
					cell2.setCellStyle(StyleFactory.getDateStyle(book));
				}else if(val instanceof String){
					cell2.setCellType(Cell.CELL_TYPE_STRING);
					cell2.setCellValue(val.toString());
					cell2.setCellStyle(StyleFactory.getContentStyle(book));
				}else{
					cell2.setCellType(Cell.CELL_TYPE_NUMERIC);
					BigDecimal db = new BigDecimal(val.toString());
					cell2.setCellValue(db.doubleValue());
					cell2.setCellStyle(StyleFactory.getNumberStyle(book));
				}
			}else{
				cell2.setCellType(Cell.CELL_TYPE_STRING);
				cell2.setCellValue(val.toString());
				cell2.setCellStyle(StyleFactory.getContentStyle(book));
			}
		}
	}
	public void writeHead(List<String> head, Row row, Workbook book){
		int celId = 0;
		for(String key : head){
			Cell cell2 = row.createCell(celId++);
			cell2.setCellType(Cell.CELL_TYPE_STRING);
			cell2.setCellValue(key);
			cell2.setCellStyle(StyleFactory.getHeadStyle(book));
		}
	}
	public void writeNull(int rowId, Sheet sheet){
		sheet.createRow(rowId);
		sheet.addMergedRegion(new CellRangeAddress(rowId, rowId, 0, 20));
	}
	public void writeTitle(int rowId, Sheet sheet, String title, Workbook book, int width){
		Cell cell2 = sheet.createRow(rowId).createCell(0);
		cell2.setCellType(Cell.CELL_TYPE_STRING);
		cell2.setCellValue(title);
		cell2.setCellStyle(StyleFactory.getTitleStyle(book));
		sheet.addMergedRegion(new CellRangeAddress(rowId, rowId, 0, 20));
	}
}
