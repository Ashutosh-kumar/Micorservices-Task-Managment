package com.ashutoku.userprofilemanageservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ashutoku.userprofilemanageservice.model.UserProfileDetail;


public interface UserProfileRepository extends CrudRepository<UserProfileDetail, Integer> {
	@Query(value="SELECT * FROM user_profile u WHERE u.user_short_id = :userShortId",nativeQuery = true)
	UserProfileDetail findUserByShortId(
	   @Param("userShortId") String userShortId);

}	