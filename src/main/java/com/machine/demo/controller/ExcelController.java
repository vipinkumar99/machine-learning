package com.machine.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.machine.demo.exception.MachineException;
import com.machine.demo.service.ExcelService;

@Controller
@RequestMapping("/api")
public class ExcelController {

	@Autowired
	private ExcelService excelService;

	@PostMapping("/readWriteMultipleExcel")
	public ResponseEntity<Resource> readWriteMulipleExcel(@RequestParam(required = false) List<String> sortOrder,
			@RequestParam(required = false) String fileType, @RequestParam(required = false) String delimeter,
			@RequestParam(required = false) List<MultipartFile> files) throws Exception {
		ResponseEntity<Resource> response = excelService.saveReadFiles(sortOrder, fileType, delimeter, files);
		return (response == null) ? null : response;
	}

	@PostMapping("/readWriteExcel")
	public ResponseEntity<Resource> readWriteExcel(@RequestParam(required = false) List<String> sortOrder,
			@RequestParam(required = false) String fileType, @RequestParam(required = false) String delimeter,
			@RequestParam(required = false) MultipartFile firstFile,
			@RequestParam(required = false) MultipartFile secondFile) throws Exception {
//		if (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls")
//				|| extension.equalsIgnoreCase("csv")) {
//			throw new MachineException("Files are not present !", HttpStatus.BAD_REQUEST);
//		}
		ResponseEntity<Resource> response = excelService.saveReadFile(sortOrder, fileType, delimeter, firstFile,
				secondFile);
		if (response == null) {
			throw new MachineException("Error in file writing !", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return (response == null) ? null : response;
	}

}
