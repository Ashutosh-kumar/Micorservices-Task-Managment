package com.ashutoku.userprofilemanageservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ashutoku.userprofilemanageservice.model.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;

public class UserProfileDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	private Integer userId;

	private String userShortId;
	private String userLastName;
	private String userFirstName;
	private String userEmail;
	private String createdBy;
	private String updatedBy;
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime createdDateTime;
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime updatedDateTime;
	private Integer roleId;
	
	private Role role;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserShortId() {
		return userShortId;
	}

	public void setUserShortId(String userShortId) {
		this.userShortId = userShortId;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

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

	@Override
	public String toString() {
		Gson gson = new Gson();
		String jsonString = gson.toJson(this);
		System.out.println(jsonString);

		return jsonString;

	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}
