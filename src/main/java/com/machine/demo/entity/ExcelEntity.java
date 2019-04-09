package com.machine.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "excel_table")
public class ExcelEntity extends BaseEntity {
	private String columnName;
	private String columnData;
	private String fileName;
	private int dataSize;
}
