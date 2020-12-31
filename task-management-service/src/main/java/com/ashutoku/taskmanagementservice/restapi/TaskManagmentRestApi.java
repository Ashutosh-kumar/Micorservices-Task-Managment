package com.ashutoku.taskmanagementservice.restapi;

import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ashutoku.taskmanagementservice.ValidateResourceCredentials;
import com.ashutoku.taskmanagementservice.dto.TaskDTO;

import com.ashutoku.taskmanagementservice.service.TaskServiceImpl;

@RestController
@RequestMapping("/task")
public class TaskManagmentRestApi {

	private static final Logger LOG = LoggerFactory.getLogger(TaskManagmentRestApi.class);
	PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Autowired
	private RestTemplate restTemplate;

	@Autowired(required = false)
	ClientHttpRequestFactory clientHttpRequestFactory;
	@Autowired
	TaskServiceImpl taskServiceImpl;

	@Autowired
	ValidateResourceCredentials validateResourceCredentials;
	/*
	 * @RequestMapping("/addtask") public boolean addNewTask() throws
	 * InterruptedException, ExecutionException {
	 * System.out.println("in addNewTas  start" );
	 * 
	 * List<String> scopes = new ArrayList<String>(); scopes.add("WRITE");
	 * scopes.add("READ"); ResourceOwnerPasswordResourceDetails resourceDetails =
	 * new ResourceOwnerPasswordResourceDetails();
	 * //ClientCredentialsResourceDetails resourceDetails = new
	 * ClientCredentialsResourceDetails(); resourceDetails.setUsername("krish");
	 * resourceDetails.setPassword(passwordEncoder.encode("krishpass"));
	 * resourceDetails.setAccessTokenUri("http://localhost:8282/oauth/token");
	 * 
	 * resourceDetails.setClientId("web");
	 * resourceDetails.setClientSecret(passwordEncoder.encode("webpass"));
	 * resourceDetails.setGrantType("password"); resourceDetails.setScope(scopes);
	 * 
	 * DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
	 * List<HttpMessageConverter<?>> conv = new
	 * ArrayList<HttpMessageConverter<?>>(); conv.add(new
	 * MappingJackson2HttpMessageConverter()); OAuth2RestTemplate restTemplate = new
	 * OAuth2RestTemplate(resourceDetails, clientContext);
	 * restTemplate.setMessageConverters(conv); //
	 * http://localhost:8082/userprofile/userprofile final OAuth2AccessToken
	 * accessToken = oAuth2RestTemplate.getAccessToken();
	 * 
	 * final String accessTokenAsString = accessToken.getValue(); Boolean role =
	 * restTemplate.getForObject(
	 * "http://localhost:5555/api/user/userprofile/getuserrole", Boolean.class);
	 * 
	 * return role; }
	 */



	@RequestMapping("/updatetaskstatus")
	public ResponseEntity<Object> updateTaskStatus(HttpServletRequest request, @RequestBody TaskDTO taskdetail)
			throws InterruptedException, ExecutionException {

		String loginuser = request.getHeader("loginuserShortId");
		return taskServiceImpl.saveTaskStatus(taskdetail, loginuser);

	}

	@RequestMapping("/updatetaskcomment")
	public ResponseEntity<Object> updateTaskComment(HttpServletRequest request, @RequestBody TaskDTO taskdetail)
			throws InterruptedException, ExecutionException {
		String loginuser = request.getHeader("loginuserShortId");
		return taskServiceImpl.saveTaskComment(taskdetail, loginuser);

	}

	@RequestMapping("/updatetaskassignto")
	public ResponseEntity<Object> updateTaskAssignTo(HttpServletRequest request,
			@RequestParam Integer taskId,@RequestParam String assignedTo)
			throws InterruptedException, ExecutionException {
		String loginuser = request.getHeader("loginuserShortId");
		return taskServiceImpl.updateTaskassignedTo(taskId,assignedTo, loginuser);

	}
	
	@RequestMapping("/viewusertask")
	public ResponseEntity<Object> getUserTaskByTaskIdTask(HttpServletRequest request, @RequestParam Integer taskId)
			throws InterruptedException, ExecutionException {

		String loginuser = request.getHeader("loginuserShortId");
		return taskServiceImpl.viewUserTaskByTaskId(taskId,  loginuser);

	}

	@RequestMapping("/getusertaskassigned")
	public ResponseEntity<Object> getUserTaskByAssignedToTask(HttpServletRequest request,
			@RequestParam String assignedTo) throws InterruptedException, ExecutionException {
		String loginuser = request.getHeader("loginuserShortId");
		ResponseEntity<Object> response = taskServiceImpl.getUserTaskByAssignedTo(assignedTo, loginuser);
		
	
		return response;
	}

	@RequestMapping(value = "/createnewtask", method = RequestMethod.POST)
	public ResponseEntity<Object> addTask(HttpServletRequest request, @RequestBody TaskDTO taskdetail) {
		LOG.info(" rest service call /createnewtask =======");
		String loginuser = request.getHeader("loginuserShortId");
		return taskServiceImpl.add(taskdetail, loginuser);
	}

	@RequestMapping(value = "/updatetask", method = RequestMethod.POST)
	public ResponseEntity<Object> updateTask(HttpServletRequest request, @RequestBody TaskDTO taskdetail) {
		LOG.info(" rest service call /updatetask...");
		String loginuser = request.getHeader("loginuserShortId");
		return taskServiceImpl.save(taskdetail,loginuser);
	}
	
	
	@RequestMapping(value = "/deletetaskbyid", method = RequestMethod.DELETE)
	public ResponseEntity<Object>  delete(HttpServletRequest request,@RequestParam Integer taskId) {
		LOG.info(" rest service call /updatetask...");
		String loginuser = request.getHeader("loginuserShortId");
		return taskServiceImpl.deleteByTaskId(taskId,loginuser);
	}

//	@RequestMapping(value = "/gettaskbyid", method = RequestMethod.GET)
//	public TaskDetail fetch(@RequestParam int taskId) {
//		LOG.info(" rest service call /gettaskbyid =======");
//		return taskServiceImpl.fetchById(taskId);
//	}
//
//	
//
//	@RequestMapping(value = "/gettaskbyassignedto", method = RequestMethod.GET)
//	public List<TaskDTO> fetchByAssignTo(@RequestParam String assignedTo) {
//		return taskServiceImpl.fetchTaskByAssinedTo(assignedTo);
//	}
//
//	@RequestMapping(value = "/gettaskbytitle", method = RequestMethod.GET)
//	public List<TaskDTO> fetchByTitle(@RequestParam String title) {
//		return taskServiceImpl.fetchTaskByTitle(title);
//	}
//
//	@RequestMapping(value = "/getalltask", method = RequestMethod.GET)
//	public List<TaskDetail> fetch() {
//		return taskServiceImpl.fetchAllProfiles();
//	}

}
