package com.machine.demo.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {
	public ResponseEntity<Resource> saveReadFiles(List<String> sortOrder, String fileType, String delimeter,
			List<MultipartFile> files) throws Exception;

	public ResponseEntity<Resource> saveReadFile(List<String> sortOrder, String fileType, String delimeter,
			MultipartFile firstFile, MultipartFile secondFile) throws Exception;
}
