package com.machine.demo.dao;

import java.util.List;

import com.machine.demo.entity.ExcelEntity;

public interface ExcelDao {
public boolean saveList(List<ExcelEntity>entities);
public List<ExcelEntity>getByColumnName(List<String>columnNames);
public int deleteByIds(List<Integer>ids);
}
