package com.machine.demo.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MachineException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpStatus status;

	public MachineException(String message) {
		super(message);
	}

	public MachineException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

}
