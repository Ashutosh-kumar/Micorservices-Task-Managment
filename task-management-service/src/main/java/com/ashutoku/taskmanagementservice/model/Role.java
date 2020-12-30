package com.ashutoku.taskmanagementservice.model;


import java.io.Serializable;
import java.util.List;



public class Role implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Integer id;
    

	
    private String name;

   
    private List<Permission> permissions;
    
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}



}