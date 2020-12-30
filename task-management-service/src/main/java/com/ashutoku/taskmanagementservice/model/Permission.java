package com.ashutoku.taskmanagementservice.model;



import java.io.Serializable;



public class Permission implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2639823218639573986L;

	
    private Integer id;
    

    private String name;
    
	
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

    
}

