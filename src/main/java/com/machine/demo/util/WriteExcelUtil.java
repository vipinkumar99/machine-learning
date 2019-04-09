package com.machine.demo.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WriteExcelUtil {

	public static <T> ResponseEntity<Resource> getExcelResponse(Map<String, List<String>> data, String fileName)
			throws Exception {
		Resource resource = new ByteArrayResource(createExcelFile(data).toByteArray());
		return ResponseEntity.ok()
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".xlsx").body(resource);
	}

	public static ByteArrayOutputStream createExcelFile(Map<String, List<String>> hashmap) throws IOException {
		@SuppressWarnings("resource")
		Workbook workbook = new XSSFWorkbook();
		ByteArrayOutputStream fileStream = new ByteArrayOutputStream();
		Sheet sheet = workbook.createSheet();
		sheet.setDefaultColumnWidth(18);
		int column = 0;
		Row header = sheet.createRow(column);
		writeCell(hashmap.keySet().stream().collect(Collectors.toList()), header);
		column++;
		int columnLength = hashmap.values().stream().mapToInt(r -> r.size()).max().getAsInt();
		for (int j = 0; j < columnLength; j++) {
			Row row = sheet.createRow(column);
			int index = 0;
			for (Entry<String, List<String>> map : hashmap.entrySet()) {
				List<String> value = map.getValue();
				int size = value.size() - 1;
				if (size >= j) {
					writeCellWithIndex(row, index, value.get(j));
					index++;
				} else {
					index++;
				}
			}
			column++;
		}
		workbook.write(fileStream);
		log.info("Excel data write successfully !");
		return fileStream;
	}

	private static void writeCell(List<String> data, Row row) {
		int index = 0;
		for (String s : data) {
			if (s != null && !s.equals("")) {
				Cell cell = row.createCell(index);
				cell.setCellValue(s);
			}
			index++;
		}
	}

	private static void writeCellWithIndex(Row row, int index, String data) {
		Cell cell = row.createCell(index);
		cell.setCellValue(data);
	}
}
