package com.management.system.dto;

public class SingleChatReqest {

	private Integer employeeId;
	private Integer receiverId;

	public SingleChatReqest() {
		// TODO Auto-generated constructor stub
	}

	public SingleChatReqest(Integer employeeId, Integer receiverId) {
		super();
		this.employeeId = employeeId;
		this.receiverId = receiverId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

}