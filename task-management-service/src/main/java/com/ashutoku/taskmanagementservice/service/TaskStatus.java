package com.ashutoku.taskmanagementservice.service;

public enum TaskStatus {

    TO_DO {
        @Override
        public TaskStatus[] nextState() {
            return new TaskStatus [] {IN_PROGRESS};
        }
        @Override
        public TaskStatus[] prevState() {
            return  new TaskStatus [] {};
        }

        @Override
        public String responsiblePerson() {
            return "ROLE_USER";
        }
    },
    IN_PROGRESS {
        @Override
        public TaskStatus[] nextState() {
            return new TaskStatus [] {READY_TO_REVIEW,TO_DO};
        }
        @Override
        public TaskStatus[] prevState() {
            return new TaskStatus [] {TO_DO};
        }

        @Override
        public String responsiblePerson() {
            return "ROLE_USER";
        }
    },
    READY_TO_REVIEW {
        @Override
        public TaskStatus[] nextState() {
            return new TaskStatus [] {NEED_CHANGE,COMPLETED,IN_PROGRESS} ;
        }
        
        @Override
        public TaskStatus[] prevState() {
            return new TaskStatus [] {IN_PROGRESS};
        }


        @Override
        public String responsiblePerson() {
            return "ROLE_ADMIN";
        }
    },
    NEED_CHANGE {
        @Override
        public TaskStatus[] nextState() {
            return new TaskStatus [] {IN_PROGRESS} ;
        }
        @Override
        public TaskStatus[] prevState() {
            return new TaskStatus [] { READY_TO_REVIEW};
        }

        @Override
        public String responsiblePerson() {
            return "ROLE_USER";
        }
    },


	COMPLETED {
        @Override
        public TaskStatus[] nextState() {
        	 return new TaskStatus [] {TO_DO};
        }
        @Override
        public TaskStatus[] prevState() {
        	
            return new TaskStatus [] { READY_TO_REVIEW};
        }

        @Override
        public String responsiblePerson() {
            return "ROLE_ADMIN";
        }
    };
    public abstract TaskStatus[]  nextState(); 
    public abstract TaskStatus[] prevState(); 
    public abstract String responsiblePerson();
    
    public static TaskStatus getEnumFromString(String status) {
    	switch (status) {
    		case "TO_DO" : return TaskStatus.TO_DO;
    		case "IN_PROGRESS" : return TaskStatus.IN_PROGRESS;
    		case "READY_TO_REVIEW" : return TaskStatus.READY_TO_REVIEW;
    		case "NEED_CHANGE" : return TaskStatus.NEED_CHANGE;
    		case "COMPLETED" : return TaskStatus.COMPLETED;
    		
    	}
		return null;
    	 	
    }
}