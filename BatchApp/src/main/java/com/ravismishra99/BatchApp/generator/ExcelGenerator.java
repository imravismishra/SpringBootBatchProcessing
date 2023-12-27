package com.ravismishra99.BatchApp.generator;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ravismishra99.BatchApp.entity.Product;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ExcelGenerator {

	private List<Product> productList;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public ExcelGenerator(List<Product> productList)
	{
		this.productList=productList;
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeader()
	{
		sheet = workbook.createSheet("Product");
		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(18);
		font.setItalic(true);
		font.setBold(true);
		font.setColor(Font.COLOR_RED);

		createCell(row,0,"id",style);
		createCell(row,1,"title",style);
		createCell(row,2,"description",style);
		createCell(row,3,"image1",style);
		createCell(row,4,"image2",style);
		createCell(row,5,"image3",style);
		createCell(row,6,"createdAt",style);
		createCell(row,7,"updatedAt",style);
		createCell(row,8,"category_id",style);
		createCell(row,9,"category_name",style);
		createCell(row,10,"category_image",style);
		createCell(row,11,"category_createdAt",style);
		createCell(row,12,"category_updatedAt",style);

	}
	
	private void createCell(Row row,int column,Object value,CellStyle style) {
		sheet.autoSizeColumn(column);
		Cell cell = row.createCell(column);
		if(value instanceof Integer)
		{
			cell.setCellValue((Integer)value);
		}
		if(value instanceof Long)
		{
			cell.setCellValue((Long)value);
		}
		if(value instanceof String)
		{
			cell.setCellValue((String)value);
		}
		cell.setCellStyle(style);
	}
	
	
	private void writer()
	{
		int row = 1;
		CellStyle style= workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(18);
		font.setItalic(true);
		font.setBold(true);
		
		style.setFont(font);
		for(Product product : productList)
		{
			Row r = sheet.createRow(row++);
			int column = 0;
			
			createCell(r, column++, product.getId(), style);
			createCell(r, column++, product.getTitle(), style);
			createCell(r, column++, product.getDescription(), style);
			createCell(r, column++, product.getImage1(), style);
			createCell(r, column++, product.getImage2(), style);
			createCell(r, column++, product.getImage3(), style);
			createCell(r, column++, product.getCreatedAt(), style);
			createCell(r, column++, product.getUpdatedAt(), style);
			createCell(r, column++, product.getCategory_id(), style);
			createCell(r, column++, product.getCategory_name(), style);
			createCell(r, column++, product.getCategory_image(), style);
			createCell(r, column++, product.getCategory_createdAt(), style);
			createCell(r, column++, product.getCategory_updatedAt(), style);
			
		}
	}
	
	public void generateExcelFile(HttpServletResponse response) throws IOException
	{
		writeHeader();
		writer();
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
	}
	
}
