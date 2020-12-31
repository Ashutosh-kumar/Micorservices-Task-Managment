package com.ashutoku.taskmanagementservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ashutoku.taskmanagementservice.ValidateResourceCredentials;
import com.ashutoku.taskmanagementservice.dto.TaskDTO;
import com.ashutoku.taskmanagementservice.dto.UserProfileDTO;
import com.ashutoku.taskmanagementservice.model.TaskDetail;
import com.ashutoku.taskmanagementservice.repo.TaskRepository;
import com.ashutoku.taskmanagementservice.restapi.error.ApiError;
import com.ashutoku.taskmanagementservice.mailer.Mailer;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class TaskServiceImpl implements ITaskService {

	private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	ValidateResourceCredentials validateResourceCredentials;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	@HystrixCommand(fallbackMethod = "getUserAndTask_Fallback", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")
			})
	public ResponseEntity<Object> save(TaskDTO task,  String userlogin) {
		
		UserProfileDTO userandrole = callUserProfileServiceToValidateUserAndRole(userlogin);
		if (userandrole == null) {
			return returnInvalidUserResponse(userlogin);
		}
		Optional<TaskDetail> taskDetail = taskRepository.findById(task.getTaskId());

		if (taskDetail.isPresent()) {
			TaskDetail taskDetailtoSave = taskDetail.get();
			if (taskDetailtoSave.getAssignedTo().equalsIgnoreCase(userlogin)) {
				task.setUpdatedby(userlogin);
				task.setUpdatedDateTime(LocalDateTime.now());
				taskDetailtoSave = convertToEntity(task, taskDetailtoSave);
				TaskDetail taskDetailPostDbSave = taskRepository.save(taskDetailtoSave);
				sendEmailNotificationToUser(userandrole.getUserEmail(),
						"Task Detail Updated:"+taskDetailPostDbSave.getTaskId(), "Task detail updated");
				return new ResponseEntity<Object>(taskDetailPostDbSave, new HttpHeaders(), HttpStatus.OK);
			} else {
				String msg = "Task not belong to user:" + userlogin;
				LOG.info(msg);
				ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
				return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
			}
		} else {
			String msg = "Task not found in db:" + userlogin;
			LOG.info(msg);
			ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		}

	}

	@Override
	//@HystrixCommand(fallbackMethod = "getUserAndTask_Fallback", commandProperties = {
	//		   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")
	//		})
	public ResponseEntity<Object> saveTaskStatus(TaskDTO task, String userlogin) {
		UserProfileDTO userandrole = callUserProfileServiceToValidateUserAndRole(userlogin);
		
		if (userandrole == null) {
			return returnInvalidUserResponse(userlogin);
		}
		Optional<TaskDetail> taskDetail = taskRepository.findById(task.getTaskId());
		if (taskDetail.isPresent()) {
			TaskDetail taskDetailtoDbSave = taskDetail.get();
			if (!TaskStatus.READY_TO_REVIEW.equals(taskDetailtoDbSave.getStatus())
					&& "ROLE_USER".equalsIgnoreCase(userandrole.getRole().getName())) {
				if (!userlogin.equalsIgnoreCase(taskDetailtoDbSave.getAssignedTo())) {
	
					String msg = "Task not belong to user:" + userlogin;
					LOG.info(msg);
					ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
					return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	
				}
			}
			
			if (validateTaskStatus(task.getStatus(), taskDetailtoDbSave.getStatus(), userandrole.getRole().getName())) {
				taskDetailtoDbSave.setStatus(task.getStatus());
				if (TaskStatus.COMPLETED.equals(task.getStatus())){
					taskDetailtoDbSave.setCompleteDateTime(LocalDateTime.now());
				}
				taskDetailtoDbSave.setUpdatedby(userlogin);
				taskDetailtoDbSave.setUpdatedDateTime(LocalDateTime.now());
				TaskDetail taskDetailPostSave = taskRepository.save(taskDetailtoDbSave);
				sendEmailNotificationToUser(userandrole.getUserEmail(),
						"Task Detail Updated:"+taskDetailPostSave.getTaskId(), "Task Status updated");
				
				return new ResponseEntity<Object>(taskDetailPostSave, new HttpHeaders(), HttpStatus.OK);
			} else {
				String msg = "Task status invalid for the role:" + userandrole.getRole().getName();
				LOG.info(msg);
				ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
				return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
			}
		} else {
			String msg = "Task id not present";
			LOG.info(msg);
			ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

		}

	}

	@Override
	@HystrixCommand(fallbackMethod = "getUserAndTask_Fallback", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")
			})
	public ResponseEntity<Object> saveTaskComment(TaskDTO task, String userlogin) {
		UserProfileDTO userandrole = callUserProfileServiceToValidateUserAndRole(userlogin);
		if (userandrole == null) {
			return returnInvalidUserResponse(userlogin);
		}
		Optional<TaskDetail> taskDetail = taskRepository.findById(task.getTaskId());
		if (taskDetail.isPresent()) {
			TaskDetail taskDetailtoSave = taskDetail.get();
			if (!(userlogin.equalsIgnoreCase(taskDetailtoSave.getAssignedTo()) && "ROLE_USER".equalsIgnoreCase(userandrole.getRole().getName()))) {

				String msg = "Task not belong to user:" + userlogin;
				LOG.info(msg);
				ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
				return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

			}

			taskDetailtoSave.setComment(task.getComment());
			taskDetailtoSave.setUpdatedby(userlogin);
			taskDetailtoSave.setUpdatedDateTime(LocalDateTime.now());
			TaskDetail taskDetailPostSave = taskRepository.save(taskDetailtoSave);
			return new ResponseEntity<Object>(taskDetailPostSave, new HttpHeaders(), HttpStatus.OK);
		} else {
			String msg = "Task id not present";
			LOG.info(msg);
			ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

		}

	}

	@Override
	@HystrixCommand(fallbackMethod = "getUserAndTask_Fallback", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")
			})
	public ResponseEntity<Object> viewUserTaskByTaskId(Integer taskId, String userlogin) {
		UserProfileDTO userandrole = callUserProfileServiceToValidateUserAndRole(userlogin);
		if (userandrole == null) {
			return returnInvalidUserResponse(userlogin);
		}
		Optional<TaskDetail> taskDetail = taskRepository.findById(taskId);
		if (taskDetail.isPresent()) {
			TaskDetail taskDetailFromDb = taskDetail.get();
			if (userlogin.equalsIgnoreCase(taskDetailFromDb.getAssignedTo()) && "ROLE_USER".equalsIgnoreCase(userandrole.getRole().getName())) {
				return new ResponseEntity<Object>(taskDetailFromDb, new HttpHeaders(), HttpStatus.OK);
			} else {
				String msg = "Task not belong to user:" + userlogin;
				LOG.info(msg);
				ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
				return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
			}
		} else {
			String msg = "Task id not present";
			LOG.info(msg);
			ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

		}

	}

	@Override
	
	@HystrixCommand(fallbackMethod = "getUserAndTask_Fallback", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")
			})
	public ResponseEntity<Object> getUserTaskByAssignedTo(String assignedTo, String userlogin) {

		UserProfileDTO userandrole = callUserProfileServiceToValidateUserAndRole(userlogin);
		if (userandrole == null) {
			return returnInvalidUserResponse(userlogin);
		}
	
		if (userlogin.equalsIgnoreCase(assignedTo) && "ROLE_USER".equalsIgnoreCase(userandrole.getRole().getName())) {
			List<TaskDTO> list = fetchTaskByAssinedTo(assignedTo);
			if (list.size() > 0) {
				return new ResponseEntity<Object>(list, new HttpHeaders(), HttpStatus.OK);
			} else {
				String msg = "Task(s) not present for user:" + assignedTo;
				LOG.info(msg);
				ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
				return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

			}
		} else {
			String msg = "Task for self can be searched only:" + userlogin;
			LOG.info(msg);
			ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		}
	}
	
	@SuppressWarnings("unused")
	private ResponseEntity<Object>	getUserAndTask_Fallback(String assignedTo, String userlogin) {
		String msg = "User service is down,cannot validate user Service will be back shortly !!! Fallback enabled for user :" + userlogin;
		LOG.info(msg);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		
	}
	
	

	@Override
	@HystrixCommand(fallbackMethod = "getUserAndTask_Fallback", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")
			})
	public ResponseEntity<Object> add(TaskDTO taskDto, String userlogin) {
		UserProfileDTO userandrole = callUserProfileServiceToValidateUserAndRole(userlogin);
		if (userandrole == null) {
			return returnInvalidUserResponse(userlogin);
		}
		if (userandrole.getRole() != null) {
			TaskDetail task = new TaskDetail();
			task.setCreatedDateTime(LocalDateTime.now());
			task.setCreatedby(userlogin);
			task.setAssignedTo(userlogin);
			taskDto.setUpdatedby(userlogin);
			taskDto.setStatus(TaskStatus.TO_DO.toString());
			task = convertToEntity(taskDto, task);
			TaskDetail taskdetail = taskRepository.save(task);
			sendEmailNotificationToUser(userandrole.getUserEmail(),
					"Task Detail Updated:"+taskdetail.getTaskId(), "New Task Created and assigned.");
			
			return new ResponseEntity<Object>(taskdetail, new HttpHeaders(), HttpStatus.OK);
		} else {
			String msg = "User Not Authorised to  add request for task";
			LOG.info(msg);
			ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

		}
	}
	
	@Override
	@HystrixCommand(fallbackMethod = "getUserAndTask_Fallback", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")
			})
	public ResponseEntity<Object> deleteByTaskId(Integer taskId,  String loginuser) {
		UserProfileDTO userandrole = callUserProfileServiceToValidateUserAndRole(loginuser);
		if (userandrole == null) {
			return returnInvalidUserResponse(loginuser);
		}
		Optional<TaskDetail> taskDetail = taskRepository.findById(taskId);
		if (taskDetail.isPresent()) {
			TaskDetail taskDetailDb = taskDetail.get();
			if (taskDetailDb.getAssignedTo().equalsIgnoreCase(loginuser) && "ROLE_USER".equalsIgnoreCase(userandrole.getRole().getName())) {
				taskRepository.deleteById(taskId);
				sendEmailNotificationToUser(userandrole.getUserEmail(),
						"Task Detail Updated:"+taskDetailDb.getTaskId(), "Task Deleted.");
				
				return new ResponseEntity<Object>("Task Deleted", new HttpHeaders(), HttpStatus.OK);
			}else {
				String msg = "User Not Authorised to  add request for task";
				LOG.info(msg);
				ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
				return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

			}
		} else {
			String msg = "Task not found";
			LOG.info(msg);
			ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
			return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

		}
		
	}
	
	@Override
	@HystrixCommand(fallbackMethod = "getUserAndTask_Fallback", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000")
			})
	public ResponseEntity<Object> updateTaskassignedTo(Integer taskId, String assignedTo, String loginuser) {
		UserProfileDTO userandrole = callUserProfileServiceToValidateUserAndRole(loginuser);
		if (userandrole == null) {
			return returnInvalidUserResponse(loginuser);
		}
		
		
			Optional<TaskDetail> taskDetail = taskRepository.findById(taskId);
			if (taskDetail.isPresent()) {
				TaskDetail taskDetailsTosave  = taskDetail.get();
				taskDetailsTosave.setAssignedTo(assignedTo);
				taskDetailsTosave.setUpdatedby(loginuser);
				taskDetailsTosave.setUpdatedDateTime(LocalDateTime.now());
				TaskDetail taskdetailPostSave = taskRepository.save(taskDetailsTosave);
				sendEmailNotificationToUser(userandrole.getUserEmail(),
						"Task Detail Updated:"+taskdetailPostSave.getTaskId(), "Task assignee has changed.");
				
				return new ResponseEntity<Object>(taskdetailPostSave, new HttpHeaders(), HttpStatus.OK);
			} else {
				String msg = "Task(s) not present for user:" + assignedTo;
				LOG.info(msg);
				ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
				return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

			}
		
	}



	private TaskDetail convertToEntity(TaskDTO taskDto, TaskDetail taskDetail) {
		taskDetail.setBody(taskDto.getBody());
		taskDetail.setTitle(taskDto.getTitle());
		taskDetail.setComment(taskDto.getComment());
		taskDetail.setUpdatedby(taskDto.getUpdatedby());
		taskDetail.setUpdatedDateTime(LocalDateTime.now());
		return taskDetail;
	}

	
	private List<TaskDTO> fetchTaskByAssinedTo(String assignedTo) {
		List<TaskDetail> taskList = taskRepository.findTaskByAssignedTo(assignedTo);
		final List<TaskDTO> list = new ArrayList<TaskDTO>();
		taskList.forEach(tl -> {
			TaskDTO taskDTO = new TaskDTO();
			taskDTO.setTaskId(tl.getTaskId());
			taskDTO.setTitle(tl.getTitle());
			taskDTO.setBody(tl.getBody());
			taskDTO.setComment(tl.getComment());
			taskDTO.setAssignedTo(tl.getAssignedTo());
			taskDTO.setStatus(tl.getStatus());
			taskDTO.setUpdatedby(tl.getUpdatedby());
			taskDTO.setUpdatedDateTime(tl.getUpdatedDateTime());
			taskDTO.setCreatedBy(tl.getCreatedby());
			taskDTO.setCreatedDateTime(tl.getCreatedDateTime());
			list.add(taskDTO);
		});
		return list;
	}

//	@Override
//	public List<TaskDTO> fetchTaskByTitle(String title) {
//		List<TaskDetail> taskList = taskRepository.findTaskByTitle(title);
//		System.out.println("taskList==============" + taskList);
//		final List<TaskDTO> list = new ArrayList<TaskDTO>();
//		taskList.forEach(tl -> {
//			TaskDTO taskDTO = new TaskDTO();
//			taskDTO.setTaskId(tl.getTaskId());
//			taskDTO.setTitle(tl.getTitle());
//			taskDTO.setBody(tl.getBody());
//			taskDTO.setComment(tl.getComment());
//			taskDTO.setAssignedTo(tl.getAssignedTo());
//			taskDTO.setStatus(tl.getStatus());
//			taskDTO.setUpdatedby(tl.getUpdatedby());
//			taskDTO.setUpdatedDateTime(tl.getUpdatedDateTime());
//			taskDTO.setCreatedBy(tl.getCreatedby());
//			taskDTO.setCreatedDateTime(tl.getCreatedDateTime());
//
//			list.add(taskDTO);
//		});
//		return list;
//	}

	

	private boolean validateTaskStatus(String taskNewStatus, String taskOldStatus, String role) {
		TaskStatus enumNewStatus = TaskStatus.getEnumFromString(taskNewStatus);
		TaskStatus enumOldStatus = TaskStatus.getEnumFromString(taskOldStatus);
		if ( Arrays.asList(enumOldStatus.nextState()).contains(enumNewStatus)) {
			if (enumOldStatus.responsiblePerson().equals(role)) {
				return true;
			}
		}
		return false;
	}
	
	private UserProfileDTO callUserProfileServiceToValidateUserAndRole(String loginuser) {
		String access_token = validateResourceCredentials.getResourceCredentialsAccessToken();
		HttpEntity<?> entity = validateResourceCredentials.createHeaderForResourceCredential(access_token);

		UserProfileDTO userandrole = validateResourceCredentials.getForObjectUsingAccessToken(
				"http://user-profile-service/userprofile/getuseandrrole?userProfileShortId=" + loginuser, UserProfileDTO.class,
				restTemplate, entity);
		
		return userandrole;
	}
	
	private void sendEmailNotificationToUser(String emailTo,String subject, String message ) {
		try {
			Mailer.sendEmailNotification(emailTo, subject, message);
		}catch (Exception ex) {
			LOG.error("Error in sending email to the user.");
			LOG.error(ex.getMessage());
			
		}
	}
	private ResponseEntity<Object> returnInvalidUserResponse(String userlogin) {
		String msg = "Invalid user:" + userlogin;
		LOG.info(msg);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	
	}
	
	@SuppressWarnings("unused")
	private ResponseEntity<Object>	getUserAndTask_Fallback(TaskDTO task, String userlogin) {
		String msg = "User service is down and cannot update,cannot validate user Service will be back shortly !!! Fallback enabled for user :" + userlogin;
		LOG.info(msg);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		
	}
	
	@SuppressWarnings("unused")
	private ResponseEntity<Object>	getUserAndTask_Fallback(Integer taskId, String userlogin) {
		String msg = "User service is down and cannot search tasks,cannot validate user Service will be back shortly !!! Fallback enabled for user :" + userlogin;
		LOG.info(msg);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		
	}
	
	@SuppressWarnings("unused")
	private ResponseEntity<Object>	getUserAndTask_Fallback(Integer taskId, String assignedTo,String userlogin) {
		String msg = "User service is down and cannot update assignee,cannot validate user Service will be back shortly !!! Fallback enabled for user :" + userlogin;
		LOG.info(msg);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, msg, "error occurred");
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
		
	}

	
}
