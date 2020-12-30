package com.ashutoku.userprofilemanageservice.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ashutoku.userprofilemanageservice.model.Role;



public interface RoleRepository extends CrudRepository<Role, Integer>{
	Optional<Role> findById(Integer roleId);
}
