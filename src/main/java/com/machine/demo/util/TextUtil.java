package com.machine.demo.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class TextUtil {
	public static String NEW_LINE = System.getProperty("line.separator");

	public static <T> ResponseEntity<Resource> getTextResponse(Map<String, List<String>> data, String fileName,
			String delimeter) throws Exception {
		Resource resource = new ByteArrayResource(createTextFile(data, delimeter).toByteArray());
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/plain;charset=UTF-8"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".txt").body(resource);
	}

	public static ByteArrayOutputStream createTextFile(Map<String, List<String>> hashmap, String delimeter)
			throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int columnLength = hashmap.values().stream().mapToInt(r -> r.size()).max().getAsInt();
		String line = new String(new char[50]).replace('\0', '-');
		baos.write(line.getBytes());
		baos.write(NEW_LINE.getBytes());
		baos.write(formatString(hashmap.keySet().stream().collect(Collectors.toList()), delimeter).getBytes());
		for (int j = 0; j < columnLength; j++) {
			List<String> data = new ArrayList<>();
			for (Entry<String, List<String>> map : hashmap.entrySet()) {
				List<String> value = map.getValue();
				int size = value.size() - 1;
				if (size >= j) {
					data.add(value.get(j));
				} else {
					data.add(" ");
				}
			}
			baos.write(formatString(data, delimeter).getBytes());
		}
		baos.write(line.getBytes());
		baos.close();
		return baos;
	}

	public static String getExtensionName(String fileName) {
		Optional<String> optional = Optional.ofNullable(fileName).filter(f -> f.contains("."))
				.map(f -> f.substring(fileName.lastIndexOf(".") + 1));
		return optional.isPresent() ? optional.get() : null;
	}

	private static String formatString(List<String> data, String delimeter) {
		StringBuilder sb = new StringBuilder();
		sb.append(delimeter);
		for (String s : data) {
			// String format = String.format(" %1$-20s ", s);
			// sb.append(format);
			sb.append(s);
			sb.append(delimeter);
		}
		sb.append(NEW_LINE);
		System.out.print(sb.toString());
		return sb.toString();
	}
}
