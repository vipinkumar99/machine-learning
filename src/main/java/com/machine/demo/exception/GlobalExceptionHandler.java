package com.machine.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<MachineErrorResponse> handleMachineException(MachineException exception) {
		MachineErrorResponse response = new MachineErrorResponse();
		response.setStatus(exception.getStatus().value());
		response.setMesage(exception.getMessage());
		response.setTimetamp(System.currentTimeMillis());
		exception.printStackTrace();
		return new ResponseEntity<>(response, exception.getStatus());
	}

	@ExceptionHandler
	public ResponseEntity<MachineErrorResponse> handleException(Exception exception) {
		MachineErrorResponse response = new MachineErrorResponse();
		response.setStatus(HttpStatus.OK.value());
		response.setMesage(exception.getMessage());
		response.setTimetamp(System.currentTimeMillis());
		exception.printStackTrace();
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
