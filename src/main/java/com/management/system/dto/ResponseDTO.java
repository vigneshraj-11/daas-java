package com.management.system.dto;

public class ResponseDTO {

	private String message;
	private boolean status;

	public ResponseDTO() {
		// TODO Auto-generated constructor stub
	}

	public ResponseDTO(String message, boolean status) {
		super();
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}