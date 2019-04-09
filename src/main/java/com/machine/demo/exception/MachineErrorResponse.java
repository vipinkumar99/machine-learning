package com.machine.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MachineErrorResponse {
	private int status;
	private String mesage;
	private long timetamp;

}
