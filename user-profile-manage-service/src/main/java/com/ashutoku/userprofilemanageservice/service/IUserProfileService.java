package com.ashutoku.userprofilemanageservice.service;

import java.util.List;


import com.ashutoku.userprofilemanageservice.dto.UserProfileDTO;
import com.ashutoku.userprofilemanageservice.model.Role;
import com.ashutoku.userprofilemanageservice.model.UserProfileDetail;

public interface IUserProfileService {
	public UserProfileDetail save(UserProfileDTO userProfileDetail);
	
    boolean isUserUpdatingOwnProfile (String userProfileShortId);
    
    public UserProfileDetail fetchByShortId(String userProfileShortId);

    public List<UserProfileDetail> fetchAllUserProfiles();
	
    public UserProfileDTO fetchUserAndRole(String userProfileShortId);
	
	public String fetchUserEmail(String userProfileShortId);
//	
//	List<UserProfileDTO> fetchUserByLastName(String LastName);
//	
	public UserProfileDetail add(UserProfileDTO userProfileDto);
//	
	public void deleteByUserId(Integer userId); 
}
