package com.hiroakina.ToDoList;

import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.time.LocalDate;
import javax.swing.table.AbstractTableModel;


public class TaskManager extends AbstractTableModel{
	
	private List<Task> tasks; // List to store tasks
	private final String[] columnNames = {"Task", "Category", "Priority", "Due date", "Completed"}; // Column names for the table
	
	public TaskManager() {
        tasks = new ArrayList<>();
    }
	
	public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }
    
    public int getRowCount() {
        return tasks.size();
    }

    public int getColumnCount() {
    	return columnNames.length;
    }
    
    //Get the value at a specific row and column.
    public Object getValueAt(int rowIndex, int columnIndex) {
    	Task task = tasks.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return task.getTask();
            case 1:
                return task.getCategory();
            case 2:
                return task.getPriority();
            case 3:
                return task.getDueDate();
            case 4:
                return task.isCompleted();
            default:
                return null;
        }
    }
    
    //Set the value at a specific row and column.
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	Task task = tasks.get(rowIndex);
        switch (columnIndex) {
	        case 0:
	            task.setTask(aValue.toString());
	            break;
	        case 1:
	            task.setCategory(aValue.toString());
	            break;
	        case 2:
	            task.setPriority(aValue.toString());
	            break;
	        case 3:
	                task.setDueDate((LocalDate) aValue);
	            break;
	        case 4:
	            task.setCompleted(Boolean.valueOf(aValue.toString()));
	            break;
	    }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    //Get the class type of a specific column.
    
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: // task
            case 1: // Category
            case 2: // priority
            	return String.class;                
            case 3: // Due date
            	return LocalDate.class;
            case 4://Complete
            	return Boolean.class;
            default:
                return Object.class;
        }
    }
    
	//Determine if a cell is editable.
    public boolean isCellEditable(int rowIndex, int columnIndex){
    	// Only the 'Completed' column is editable
    	if(columnIndex==4) {
    		return true;
    	}else {
    		return false;
    	}
    	
    }
    
    //Add a new task to the list.
    public void addTask(String task,String category, String priority,int year,int month, int day) {
        LocalDate dueDate = LocalDate.of(year,month,day);
        Task newTask = new Task(task, category, priority, dueDate, false);
        tasks.add(newTask);
        
        fireTableRowsInserted(tasks.size() - 1, tasks.size() - 1);
    }
    
    //Load tasks from a file.
    public boolean loadTasks(String filename){
        File file = new File(filename);
        // If the file does not exist
        if (!file.exists()) {
            return false;
        }

        // Read the file
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof List<?>) {
                tasks = (List<Task>) obj;
                fireTableDataChanged();
                return true; // Successfully loaded
            }
        } catch (IOException | ClassNotFoundException e) {
            tasks = new ArrayList<>(); // Initialize with an empty list if loading fails
            return false; // Loading error
        }
        return false;
    }
    
    
    //Save tasks to a file.
    public void saveTasks(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(tasks);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }
    
    //Remove a task from the list.
    public void removeTask(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < tasks.size()) {
            tasks.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    //Get a set of all unique categories from the tasks.
    public Set<String> getCategories() {
        Set<String> categories = new HashSet<>();
        for (Task task : tasks) {
            categories.add(task.getCategory());
        }
        return categories;
    }
}
