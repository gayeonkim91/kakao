package com.kakao.interfaces.controller;

import com.kakao.interfaces.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class KakaoApiControllerAdvice {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseDTO exceptionHandler(MethodArgumentNotValidException e) {
		e.printStackTrace();
		ObjectError error = e.getBindingResult().getAllErrors().stream().findAny().orElse(null);
		return ResponseDTO.ofFailure(error == null ? Strings.EMPTY : error.getDefaultMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseDTO exceptionHandler(Exception e) {
		e.printStackTrace();
		return ResponseDTO.ofFailure(e.getMessage());
	}
}
