package com.machine.demo.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelUtil {

	@SuppressWarnings("resource")
	public static Map<String, List<String>> readExcelData(byte[] bytes) throws Exception {
		if (bytes == null || bytes.length <= 0) {
			return null;
		}
		Map<String, List<String>> data = null;
		XSSFWorkbook workbook = null;
		ByteArrayInputStream fis = new ByteArrayInputStream(bytes);
		try {
			workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheetAt(0);
			data = columnData(sheet);
		} catch (Exception e) {
			return null;
		} finally {
			fis.close();
		}
		return data;
	}

	private static Map<String, List<String>> columnData(Sheet sheet) {
		Row firstRow = sheet.getRow(0);
		Map<Integer, String> headerMap = readHeaderRow(firstRow);
		sheet.removeRow(firstRow);
		Map<String, List<String>> column = new LinkedHashMap<>();
		for (Row row : sheet) {
			for (Cell cell : row) {
				String columnName = headerMap.get(cell.getColumnIndex());
				List<String> columnData = column.getOrDefault(columnName, new ArrayList<>());
				columnData.add(cell.getStringCellValue());
				column.put(columnName, columnData);
			}
		}
		return column;
	}

	private static Map<Integer, String> readHeaderRow(Row row) {
		Map<Integer, String> header = new HashMap<>();
		for (Cell cell : row) {
			header.put(cell.getColumnIndex(), cell.getStringCellValue());
		}
		return header;
	}

}
