package com.ppessoas.api.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ppessoas.api.model.People;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	  @ExceptionHandler(Exception.class)
	  public ResponseEntity handleException(Exception ex) {

	    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	  
	  @ExceptionHandler(ResourceNotFoundException.class)
	  public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException ex) {
		ResponseEntity responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    return new ResponseEntity(HttpStatus.NOT_FOUND);
	  }
	
}