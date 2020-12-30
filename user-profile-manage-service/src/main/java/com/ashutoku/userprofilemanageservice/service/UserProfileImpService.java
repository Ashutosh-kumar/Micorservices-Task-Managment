package com.ashutoku.userprofilemanageservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.ashutoku.userprofilemanageservice.dto.UserProfileDTO;
import com.ashutoku.userprofilemanageservice.mailer.Mailer;
import com.ashutoku.userprofilemanageservice.model.Role;
import com.ashutoku.userprofilemanageservice.model.UserProfileDetail;
import com.ashutoku.userprofilemanageservice.repo.RoleRepository;
import com.ashutoku.userprofilemanageservice.repo.UserProfileRepository;

@Service
public class UserProfileImpService implements IUserProfileService {
	private static final Logger LOG = LoggerFactory.getLogger(UserProfileImpService.class);

	@Autowired
	UserProfileRepository userProfileRepository;
	
	@Autowired
	RoleRepository roleRepository;


	@Override
	public UserProfileDetail save(UserProfileDTO userProfile) {
		UserProfileDetail userProfileDetail = userProfileRepository.findUserByShortId(userProfile.getUserShortId());
		if (userProfileDetail != null) {
			UserProfileDetail userProfileDetailtoSave = null;
			userProfileDetailtoSave = convertToEntity(userProfile, userProfileDetail);
			return userProfileRepository.save(userProfileDetailtoSave);
		} else {
			LOG.error("User Profile not present");
			return null;
		}

	}

	@Override
	public UserProfileDetail add(UserProfileDTO userProfileDto) {

		UserProfileDetail user = new UserProfileDetail();
		user.setCreatedDateTime(LocalDateTime.now());
		user.setCreatedBy(userProfileDto.getCreatedBy());
		user.setUserEmail(userProfileDto.getUserEmail());
		user.setUserShortId(userProfileDto.getUserShortId());
		user.setCreatedDateTime(LocalDateTime.now());
		user = convertToEntity(userProfileDto, user);
		sendEmailNotificationToUser(user.getUserEmail(),"New User Registered","Welcome! You are registered to use the application");
		return userProfileRepository.save(user);
	}

	private UserProfileDetail convertToEntity(UserProfileDTO userProfileDto, UserProfileDetail userProfileDetail) {
		userProfileDetail.setUserLastName(userProfileDto.getUserLastName());
		userProfileDetail.setUserFirstName(userProfileDto.getUserFirstName());
		userProfileDetail.setUserEmail(userProfileDto.getUserEmail());
		userProfileDetail.setCreatedBy(userProfileDto.getCreatedBy());
		userProfileDetail.setUpdatedBy(userProfileDto.getUpdatedBy());
		userProfileDetail.setUpdatedDateTime(LocalDateTime.now());
		return userProfileDetail;
	}
	
	@Override
	public void deleteByUserId(Integer userId) {
			userProfileRepository.deleteById(userId);
	}
	
	@Override
	public List<UserProfileDetail> fetchAllUserProfiles() {
		return (List<UserProfileDetail>) userProfileRepository.findAll();
	}

	@Override
	public UserProfileDetail fetchByShortId(String userProfileShortId) {
		
		UserProfileDetail user_profile = userProfileRepository.findUserByShortId(userProfileShortId);
		return user_profile;
	}

	@Override
	public boolean isUserUpdatingOwnProfile(String userProfileShortId) {
		
		UserProfileDetail user_profile = fetchByShortId(userProfileShortId);
		if (user_profile == null) {
			return false;
		}
		boolean flag =  user_profile.getUserShortId().equalsIgnoreCase(userProfileShortId) ? true: false; 
		return flag;
	}

	@Override
	public UserProfileDTO fetchUserAndRole(String userProfileShortId) {
		UserProfileDetail user_profile = userProfileRepository.findUserByShortId(userProfileShortId);
		
		if (user_profile == null) {
			LOG.error("User Profile not present");
			return null;
		}
		
		UserProfileDTO userProfileDTO = convertEntityToDTO(user_profile);
		
		Optional<Role> role = roleRepository.findById(user_profile.getRoleId());
		if (role.isPresent()) {
			userProfileDTO.setRole(role.get());
		}
		
			return userProfileDTO;
		
		
	}
	
	@Override
	public String fetchUserEmail(String userProfileShortId) {
		UserProfileDetail user_profile = userProfileRepository.findUserByShortId(userProfileShortId);
		if (user_profile == null) {
			LOG.error("User Profile not present");
			return null;
		}
		return user_profile.getUserEmail();
	}
	
	private void sendEmailNotificationToUser(String emailTo,String subject, String message ) {
		try {
			Mailer.sendEmailNotification(emailTo, subject, message);
		}catch (Exception ex) {
			LOG.error("Error in sending email to the user.");
			LOG.error(ex.getMessage());
			
		}
	}
	
	private UserProfileDTO convertEntityToDTO(UserProfileDetail userProfileDetail) {
		UserProfileDTO dto =  new UserProfileDTO();
		dto.setUserId(userProfileDetail.getUserId());
		dto.setCreatedBy(userProfileDetail.getCreatedBy());
		dto.setCreatedDateTime(userProfileDetail.getCreatedDateTime());
		dto.setUpdatedBy(userProfileDetail.getUpdatedBy());
		dto.setUpdatedDateTime(userProfileDetail.getUpdatedDateTime());
		dto.setUserEmail(userProfileDetail.getUserEmail());
		dto.setUserFirstName(userProfileDetail.getUserFirstName());
		dto.setUserLastName(userProfileDetail.getUserLastName());
		dto.setUserShortId(userProfileDetail.getUserShortId());
		
		return dto;
		
	}
	

}
