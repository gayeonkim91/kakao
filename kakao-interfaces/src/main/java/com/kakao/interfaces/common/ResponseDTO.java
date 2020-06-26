package com.kakao.interfaces.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ResponseDTO<T> implements Serializable {
	private boolean success;
	private String message;
	private T content;

	public static <T> ResponseDTO ofSuccess(T content) {
		ResponseDTO responseDTO = new ResponseDTO<T>();
		responseDTO.success = true;
		responseDTO.content = content;
		return responseDTO;
	}

	public static ResponseDTO ofFailure(String message) {
		ResponseDTO responseDTO = new ResponseDTO<>();
		responseDTO.success = false;
		responseDTO.message = message;
		return responseDTO;
	}
}
