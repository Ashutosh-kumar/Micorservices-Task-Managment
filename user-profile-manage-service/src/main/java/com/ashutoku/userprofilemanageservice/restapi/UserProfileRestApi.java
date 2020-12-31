package com.ashutoku.userprofilemanageservice.restapi;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ashutoku.userprofilemanageservice.dto.UserProfileDTO;

import com.ashutoku.userprofilemanageservice.model.UserProfileDetail;
import com.ashutoku.userprofilemanageservice.service.IUserProfileService;



@RestController 
@RequestMapping("/userprofile")
public class UserProfileRestApi {
	private static final Logger LOG = LoggerFactory.getLogger(UserProfileRestApi.class);

	@Autowired
	IUserProfileService userProfileService; 
	
	
	@RequestMapping("/getalluserprofile")
	@PreAuthorize("hasAnyAuthority('profile_editor','profile_reader')") 
	public List<UserProfileDetail>  getUserAllProfile(HttpServletRequest request){
		LOG.info("calling getalluserprofile");
		List<UserProfileDetail>  userlist = userProfileService.fetchAllUserProfiles();
		LOG.info("in getUserProfileRole");
		return userlist;
	}
	
	@RequestMapping("/getuseandrrole")
	@PreAuthorize("hasAnyAuthority('profile_editor','profile_reader','task_reviewer','task_reader')") 
	public UserProfileDTO  getUserRole(HttpServletRequest request,@RequestParam String userProfileShortId){
		LOG.info("calling getUserRole");
		UserProfileDTO userWithRole = userProfileService.fetchUserAndRole(userProfileShortId);
		return userWithRole;
	}
	

	@RequestMapping("/adduserprofile")
	public UserProfileDetail  addNewUserProfile(@RequestBody UserProfileDTO userProfileDto){
		LOG.info("calling adduserprofile");
		UserProfileDetail userDetail = userProfileService.add(userProfileDto);
		return userDetail;
	}
	
	@RequestMapping("/updateuserprofile")
	@PreAuthorize( "hasAuthority('profile_editor')") 
	public UserProfileDetail  updateUserProfile(HttpServletRequest request,@RequestBody UserProfileDTO userProfileDto){
		LOG.info("calling updateUserProfile");
		String loginuser = request.getHeader("loginuserShortId");
		boolean updateFlag= userProfileService.isUserUpdatingOwnProfile(loginuser);
		if (updateFlag) {
			userProfileDto.setUserShortId(loginuser);
			UserProfileDetail userDetail = userProfileService.save(userProfileDto);
			return userDetail;
		}else {
			LOG.error("Not able to update , user cannot update others profile");
			return null;
		}
		
	}
	
	@RequestMapping(value = "/deleteuserbyid", method = RequestMethod.DELETE)
	@PreAuthorize( "hasAnyAuthority('profile_editor','profile_reader')") 
	public String delete(HttpServletRequest request,@RequestParam Integer userId) {
		LOG.info("calling deleteuserbyid");
		String username = request.getHeader("loginuserShortId");
		boolean updateFlag= userProfileService.isUserUpdatingOwnProfile(username);
		if (updateFlag) {
			userProfileService.deleteByUserId(userId);
			return "User Deleted.";
		}else {
			LOG.error("Not able to delete , user cannot delete other profile");
			return "Not able to delete , user cannot delete other profile";
		}
	}
	
}
