package com.ashutoku.taskmanagementservice.dto;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;

 
public class TaskDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer taskId;
	private String title;
	private String body;
	private String comment;
	private String createdBy;
	private String updatedBy;
	private String assignedTo;
	private String status;
	
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime  createdDateTime;
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime  updatedDateTime;
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime  completeDateTime;
	
	
	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	public LocalDateTime getCompleteDateTime() {
		return completeDateTime;
	}
	public void setCompleteDateTime(LocalDateTime completeDateTime) {
		this.completeDateTime = completeDateTime;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedby() {
		return updatedBy;
	}
	public void setUpdatedby(String updatedby) {
		this.updatedBy = updatedby;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/*
	public void setUpdatedDateTime(String updatedDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime dateTime = LocalDateTime.parse(updatedDateTime, formatter);
		
		this.updatedDateTime = dateTime;
	}*/
	@Override
	public String toString() {
		Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        System.out.println(jsonString);
		
		return jsonString;
		
	}

}
