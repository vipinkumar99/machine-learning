package com.machine.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.machine.demo.dao.ExcelDao;
import com.machine.demo.entity.ExcelEntity;
import com.machine.demo.exception.MachineException;
import com.machine.demo.service.ExcelService;
import com.machine.demo.util.CsvFileReadUtil;
import com.machine.demo.util.JsonUtil;
import com.machine.demo.util.ReadExcelUtil;
import com.machine.demo.util.TextUtil;
import com.machine.demo.util.WriteExcelUtil;

@Service
public class ExcelServiceImpl implements ExcelService {

	@Autowired
	private ExcelDao excelDao;

	@Override
	public ResponseEntity<Resource> saveReadFiles(List<String> sortOrder, String fileType, String delimeter,
			List<MultipartFile> files) throws Exception {
		if (CollectionUtils.isEmpty(files)) {
			throw new MachineException("Files are not present !", HttpStatus.BAD_REQUEST);
		}
		List<ExcelEntity> entities = new ArrayList<>();
		for (MultipartFile file : files) {
			List<ExcelEntity> requestList = getEntities(ReadExcelUtil.readExcelData(file.getBytes()),
					file.getOriginalFilename(), sortOrder);
			if (!CollectionUtils.isEmpty(requestList)) {
				entities.addAll(requestList);
			}
		}
		excelDao.saveList(entities);
		if (CollectionUtils.isEmpty(sortOrder)) {
			sortOrder = entities.stream().map(r -> r.getColumnName()).collect(Collectors.toList());
		}
		List<ExcelEntity> responseList = excelDao.getByColumnName(sortOrder);
		if (CollectionUtils.isEmpty(responseList)) {
			return null;
		}
		List<Integer> ids = responseList.stream().map(r -> r.getId()).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(ids)) {
			excelDao.deleteByIds(ids);
		}
		if (StringUtils.isEmpty(fileType) || fileType.equalsIgnoreCase("xlsx") || fileType.equalsIgnoreCase("csv")) {
			return WriteExcelUtil.getExcelResponse(getExcelWriteData(responseList, sortOrder), "Machine_Learning");
		} else if (fileType.equalsIgnoreCase("txt")) {
			if (StringUtils.isEmpty(delimeter)) {
				delimeter = "|";
			}
			return TextUtil.getTextResponse(getExcelWriteData(responseList, sortOrder), "Machine_Learning", delimeter);
		} else {
			return null;
		}
	}

	@Override
	public ResponseEntity<Resource> saveReadFile(List<String> sortOrder, String fileType, String delimeter,
			MultipartFile firstFile, MultipartFile secondFile) throws Exception {
		if (firstFile == null && secondFile == null) {
			throw new MachineException("Files are not present !", HttpStatus.BAD_REQUEST);
		}
		List<ExcelEntity> firstList = getEntityList(firstFile, sortOrder);
		List<ExcelEntity> entities = new ArrayList<>();
		if (!CollectionUtils.isEmpty(firstList)) {
			entities.addAll(firstList);
		}
		List<ExcelEntity> secondList = getEntityList(secondFile, sortOrder);
		if (!CollectionUtils.isEmpty(secondList)) {
			entities.addAll(secondList);
		}
		excelDao.saveList(entities);
		if (CollectionUtils.isEmpty(sortOrder)) {
			sortOrder = entities.stream().map(r -> r.getColumnName()).collect(Collectors.toList());
		}
		List<ExcelEntity> responseList = excelDao.getByColumnName(sortOrder);
		if (CollectionUtils.isEmpty(responseList)) {
			return null;
		}
		List<Integer> ids = responseList.stream().map(r -> r.getId()).collect(Collectors.toList());
		System.out.println("ids : " + ids);
		if (!CollectionUtils.isEmpty(ids)) {
			int delete = excelDao.deleteByIds(ids);
			System.out.println("delete : " + delete);
		}
		if (StringUtils.isEmpty(fileType) || fileType.equalsIgnoreCase("xlsx") || fileType.equalsIgnoreCase("csv")) {
			return WriteExcelUtil.getExcelResponse(getExcelWriteData(responseList, sortOrder), "Machine_Learning");
		} else if (fileType.equalsIgnoreCase("txt")) {
			if (StringUtils.isEmpty(delimeter)) {
				delimeter = "|";
			}
			return TextUtil.getTextResponse(getExcelWriteData(responseList, sortOrder), "Machine_Learning", delimeter);
		} else {
			return null;
		}

	}

	private List<ExcelEntity> getEntityList(MultipartFile file, List<String> sortOrder) throws Exception {
		if (file == null || file.isEmpty()) {
			return null;
		}
		String extension = TextUtil.getExtensionName(file.getOriginalFilename());
		if (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls")) {
			return getEntities(ReadExcelUtil.readExcelData(file.getBytes()), file.getOriginalFilename(), sortOrder);
		}
		if (extension.equalsIgnoreCase("csv")) {
			return getEntities(CsvFileReadUtil.readCsvData(file.getBytes()), file.getOriginalFilename(), sortOrder);
		}
		return null;
	}

	private List<ExcelEntity> getEntities(Map<String, List<String>> data, String fileName, List<String> sortOrder) {
		if (data.isEmpty()) {
			return null;
		}
		List<ExcelEntity> entitiesList = new ArrayList<>();
		if (CollectionUtils.isEmpty(sortOrder)) {
			for (Entry<String, List<String>> map : data.entrySet()) {
				entitiesList.add(convertToEntity(map.getKey(), map.getValue(), fileName, map.getValue().size()));
			}
		} else {
			for (Entry<String, List<String>> map : data.entrySet()) {
				if (sortOrder.contains(map.getKey())) {
					entitiesList.add(convertToEntity(map.getKey(), map.getValue(), fileName, map.getValue().size()));
				}
			}
		}
		return entitiesList;
	}

	private ExcelEntity convertToEntity(String columnName, List<String> data, String fileName, int size) {
		ExcelEntity request = new ExcelEntity();
		request.setFileName(fileName);
		request.setColumnName(columnName);
		request.setColumnData(JsonUtil.getJson(data));
		request.setDataSize(size);
		return request;
	}

	private Map<String, List<String>> getExcelWriteData(List<ExcelEntity> responseList, List<String> sortOrder) {
		if (CollectionUtils.isEmpty(responseList)) {
			return null;
		}
		Map<String, List<String>> map = new HashMap<>();
		for (ExcelEntity entity : responseList) {
			List<String> dataList = JsonUtil.convertToListPojo(entity.getColumnData(), String.class);
			map.put(entity.getColumnName(), dataList);
		}
		if (CollectionUtils.isEmpty(sortOrder)) {
			return map;
		} else {
			Map<String, List<String>> mapR = new LinkedHashMap<>();
			for (String s : sortOrder) {
				List<String> dataList = map.get(s);
				if (!CollectionUtils.isEmpty(dataList)) {
					mapR.put(s, dataList);
				}
			}
			return mapR;
		}
	}

}
