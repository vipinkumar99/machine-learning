package com.machine.demo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvFileReadUtil {

	public static Map<String, List<String>> readCsvData(byte[] bytes) {
		if (bytes == null || bytes.length <= 0) {
			log.info("Empty csv file data");
			return null;
		}
		String completeData = new String(bytes);
		String[] rows = completeData.split("\\r\\n");
		Map<String, List<String>> map = new HashMap<>();
		Map<Integer, String> keyMap = new HashMap<>();
		boolean isFirst = true;
		int length = 0;
		for (String row : rows) {
			String[] data = row.split(",");
			if (isFirst) {
				length = data.length;
				setFirst(map, keyMap, data);
				isFirst = false;
			} else {
				setCell(map, keyMap, data, length);
			}
		}
		log.info("Csv data read successfully ! {} ", map.size());
		return map;
	}

	private static void setFirst(Map<String, List<String>> map, Map<Integer, String> keyMap, String[] data) {
		int index = 0;
		for (String s : data) {
			map.put(s, new ArrayList<>());
			keyMap.put(index, s);
			index++;
		}
	}

	private static void setCell(Map<String, List<String>> map, Map<Integer, String> keyMap, String[] data, int length) {
		int len = data.length - 1;
		for (int i = 0; i < length; i++) {
			if (len >= i) {
				String value = data[i];
				if (!StringUtils.isEmpty(value)) {
					String key = keyMap.get(i);
					List<String> dataList = map.get(key);
					dataList.add(value);
					map.put(key, dataList);
				}
			}
		}
	}

}
