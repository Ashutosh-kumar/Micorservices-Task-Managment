package com.ashutoku.taskmanagementservice.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ashutoku.taskmanagementservice.dto.TaskDTO;
import com.ashutoku.taskmanagementservice.model.Role;
import com.ashutoku.taskmanagementservice.model.TaskDetail;

public interface ITaskService {
	ResponseEntity<Object> save(TaskDTO task,  String loginuser);
	
	ResponseEntity<Object> saveTaskStatus(TaskDTO task, String userlogin );
	
	ResponseEntity<Object> saveTaskComment(TaskDTO task,String userlogin );
	
	ResponseEntity<Object> updateTaskassignedTo(Integer taskId,String assignedTo,String loginuser);
	
	ResponseEntity<Object> viewUserTaskByTaskId(Integer taskId,String userlogin);
	
	ResponseEntity<Object> getUserTaskByAssignedTo(String assignedTo,String userlogin);
	
	ResponseEntity<Object> add(TaskDTO taskDto,  String loginuser);
	
	ResponseEntity<Object> deleteByTaskId(Integer taskId,String loginuser);
	
	/*
	TaskDetail fetchById(int profileId);

	List<TaskDetail> fetchAllProfiles();
	
	List<TaskDTO> fetchTaskByAssinedTo(String assignedTo);
	
	List<TaskDTO> fetchTaskByTitle(String title);
	*/
	
	
	
}
