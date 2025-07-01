package mini_project;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UtilsPackage {
	
	public static FileInputStream fi;
	public static FileOutputStream fo;
	public static XSSFWorkbook wb;
	public static XSSFSheet sht;
	public static XSSFRow row;
	public static XSSFCell cell;
	public static CellStyle style;
	
	public static int getRowCount(String xlfile, String xlSheet) throws IOException {
		fi = new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		sht = wb.getSheet(xlSheet);
		int rows = sht.getLastRowNum();
		wb.close();
		fi.close();
		
		return rows;
	}
	
	public static String getCellData(String xlfile, String xlsheet, int rownum, int cellnum) throws IOException {
		fi = new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		sht = wb.getSheet(xlsheet);
		row = sht.getRow(rownum);
		cell = row.getCell(cellnum);
		
		String ans;
		
		try {
			ans = cell.toString();
		}catch(Exception e) {
			ans = "";
		}
		
		wb.close();
		fi.close();
		return ans;
	}
	
	public static void setCellData(String xlfile, String xlsheet, int rownum, int cellnum, String data) throws IOException {
		fi = new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		sht = wb.getSheet(xlsheet);
		row = sht.getRow(rownum);
		cell = row.createCell(cellnum);
		cell.setCellValue(data);
		fo = new FileOutputStream(xlfile);
		wb.write(fo);
		wb.close();
		fi.close();
		fo.close();
	}
	
	public static void setGreenColor(String xlfile, String xlsheet, int rownum, int cellnum) throws IOException {
		fi = new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		sht = wb.getSheet(xlsheet);
		row = sht.getRow(rownum);
		cell = row.getCell(cellnum);
		
		style = wb.createCellStyle();
		
		style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		cell.setCellStyle(style);
		fo = new FileOutputStream(xlfile);
		wb.write(fo);
		wb.close();
		fi.close();
		fo.close();
	}
	
	public static void setRedColor(String xlfile, String xlsheet, int rownum, int cellnum) throws IOException {
		fi = new FileInputStream(xlfile);
		wb = new XSSFWorkbook(fi);
		sht = wb.getSheet(xlsheet);
		row = sht.getRow(rownum);
		cell = row.getCell(cellnum);
		
		style = wb.createCellStyle();
		
		style.setFillForegroundColor(IndexedColors.RED.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		cell.setCellStyle(style);
		
		fo = new FileOutputStream(xlfile);
		wb.write(fo);
		wb.close();
		fi.close();
		fo.close();
	}

	
	
}
