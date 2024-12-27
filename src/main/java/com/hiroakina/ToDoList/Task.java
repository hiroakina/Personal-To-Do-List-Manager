package com.hiroakina.ToDoList;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private String task;
    private String priority;
    private String category;
    private boolean isCompleted;
    private LocalDate dueDate;

    public Task(String task, String category, String priority, LocalDate dueDate, boolean isCompleted) {
        this.task = task;
        this.priority = priority;
        this.category = category;
        this.isCompleted = isCompleted;
        this.dueDate = dueDate;
    }
    
    public String getTask() {
        return task;
    }

    public String getPriority() {
        return priority;
    }
    
    public String getCategory() {
        return category;
    }

    public boolean isCompleted() {
        return isCompleted;
    } 
    
    public LocalDate getDueDate() {
    	return dueDate;
    }
    
    public void setTask(String task) {
    	this.task = task;
    }
    
    public void setPriority(String priority) {
    	this.priority = priority;
    }
    
    public void setCategory(String category) {
    	this.category = category;
    }
    
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate= dueDate;
    }
    
    
}
