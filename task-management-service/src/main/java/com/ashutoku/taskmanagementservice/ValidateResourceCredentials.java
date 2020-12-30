package com.ashutoku.taskmanagementservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
public  class ValidateResourceCredentials {
	
	private static final Logger LOG = LoggerFactory.getLogger(ValidateResourceCredentials.class);
	
	private static final Pattern pat = Pattern.compile(".*\"access_token\"\\s*:\\s*\"([^\"]+)\".*");
	
	@Autowired
	 OAuthConfig oAuthConfig;
	

	//Username is netId for the person making the call and the password is there password
	public String getResourceCredentialsAccessToken() {
		String userName = oAuthConfig.getUsername();
		String password	=	oAuthConfig.getPassword();
		String auth = oAuthConfig.getClientId()+ ":" + oAuthConfig.getClientSecret();
		String authentication = Base64.getEncoder().encodeToString(auth.getBytes());

	    String content = "grant_type=password&username=" + userName + "&password=" + password;
	    BufferedReader reader = null;
	    HttpURLConnection connection = null;
	    String returnValue = "";
	    try {
	        URL url = new URL(oAuthConfig.getTokenUrl());
	        connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Authorization", "Basic " + authentication);
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        connection.setRequestProperty("Accept", "application/json");
	        PrintStream os = new PrintStream(connection.getOutputStream());
	        os.print(content);
	        os.close();
	        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line = null;
	        StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
	        String response = out.toString();
	        Matcher matcher = pat.matcher(response);
	        if (matcher.matches() && matcher.groupCount() > 0) {
	            returnValue = matcher.group(1);
	        }
	    } catch (Exception e) {
	    	LOG.error("Error : " + e.getMessage());
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e) {
	            }
	        }
	        connection.disconnect();
	    }
	    return returnValue;
	}
	
	public HttpEntity<?> createHeaderForResourceCredential(String access_token) {
		 	HttpHeaders headers = new HttpHeaders();
		    headers.set("Authorization", "bearer "+ access_token);
		    HttpEntity<?> entity = new HttpEntity(headers);
		    return entity;
	}
	
	public <T> T getForObjectUsingAccessToken(String resourceUrl, Class<T> responseType,
			RestOperations restTemplate, HttpEntity<?> entity) {
		
		  ResponseEntity<T> re = restTemplate.exchange(
				  resourceUrl, 
		          HttpMethod.GET, 
		          entity, 
		          responseType);
		  
		     String strresponse=re.getStatusCode().equals(HttpStatus.OK) ? 
		          "ok" : "not ok";
		  
		     LOG.info(strresponse);
     	     return (T ) re.getBody();
	}
	
}
