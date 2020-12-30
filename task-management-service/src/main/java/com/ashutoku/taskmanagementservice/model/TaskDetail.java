package com.ashutoku.taskmanagementservice.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;

@Entity
@Table(name = "task_info")
public class TaskDetail {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer taskId;
	private String title;
	private String body;
	private String comment;
	private String createdBy;
	private String updatedby;
	private String assignedTo;
	private String status;
	//@DateTimeFormat(iso = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime  createdDateTime;
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime  updatedDateTime;
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime  completeDateTime;
	
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
	public String getCreatedby() {
		return createdBy;
	}
	public void setCreatedby(String createdby) {
		this.createdBy = createdby;
	}
	public String getUpdatedby() {
		return updatedby;
	}
	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
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
	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime =createdDateTime;;
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

	@Override
	public String toString() {
		Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        System.out.println(jsonString);
		
		return jsonString;
		
	}
}
