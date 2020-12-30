package com.ashutoku.taskmanagementservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ashutoku.taskmanagementservice.model.TaskDetail;


public interface TaskRepository extends CrudRepository<TaskDetail, Integer> {
	
	@Query(value="SELECT * FROM task_info t WHERE t.assigned_to = :assignedTo",nativeQuery = true)
	List<TaskDetail> findTaskByAssignedTo(
	   @Param("assignedTo") String assignedto);
	
	@Query(value="SELECT * FROM task_info t WHERE t.title like %?1% ",nativeQuery = true)
	List<TaskDetail> findTaskByTitle(
	    String title);

}	