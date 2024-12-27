package com.hiroakina.juinttest;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.hiroakina.ToDoList.TaskManager;

public class TaskManagerTest {
    private TaskManager taskManager;

    @Before
    public void setUp() {
        taskManager = new TaskManager();
    }

    @Test
    public void testAddTask() {
        taskManager.addTask("Task1", "Work", "High", 2024, 11, 30);
        assertEquals(1, taskManager.getRowCount());
        assertEquals("Task1", taskManager.getValueAt(0, 0));
        assertEquals("Work", taskManager.getValueAt(0, 1));
        assertEquals("High", taskManager.getValueAt(0, 2));
        assertEquals(LocalDate.of(2024, 11, 30), taskManager.getValueAt(0, 3));
        assertEquals(false, taskManager.getValueAt(0, 4));
    }

    @Test
    public void testRemoveTask() {
        taskManager.addTask("Task1", "Work", "High", 2024, 11, 30);
        taskManager.addTask("Task2", "Personal", "Medium", 2024, 12, 5);
        taskManager.removeTask(0);
        assertEquals(1, taskManager.getRowCount());
        assertEquals("Task2", taskManager.getValueAt(0, 0));
    }

    @Test
    public void testGetCategories() {
        taskManager.addTask("Task1", "Work", "High", 2024, 11, 30);
        taskManager.addTask("Task2", "Personal", "Medium", 2024, 12, 5);
        taskManager.addTask("Task3", "Work", "Low", 2024, 12, 15);
        Set<String> categories = taskManager.getCategories();
        assertEquals(2, categories.size());
        assertTrue(categories.contains("Work"));
        assertTrue(categories.contains("Personal"));
    }

    @Test
    public void testSaveAndLoadTasks() {
        String testFileName = "test_tasks";
        taskManager.addTask("Task1", "Work", "High", 2024, 11, 30);
        taskManager.addTask("Task2", "Personal", "Medium", 2024, 12, 5);
        taskManager.saveTasks(testFileName);

        TaskManager loadedManager = new TaskManager();
        loadedManager.loadTasks(testFileName);
        assertEquals(2, loadedManager.getRowCount());
        assertEquals("Task1", loadedManager.getValueAt(0, 0));
        assertEquals("Task2", loadedManager.getValueAt(1, 0));

        new File(testFileName).delete();
    }

    @Test
    public void testSetValueAt() {
        taskManager.addTask("Task1", "Work", "High", 2024, 11, 30);
        taskManager.setValueAt("Updated Task1", 0, 0); 
        taskManager.setValueAt("Updated Work", 0, 1); 
        taskManager.setValueAt("Low", 0, 2); 
        taskManager.setValueAt(LocalDate.of(2024, 12, 31), 0, 3); 
        taskManager.setValueAt(true, 0, 4); 

        assertEquals("Updated Task1", taskManager.getValueAt(0, 0));
        assertEquals("Updated Work", taskManager.getValueAt(0, 1));
        assertEquals("Low", taskManager.getValueAt(0, 2));
        assertEquals(LocalDate.of(2024, 12, 31), taskManager.getValueAt(0, 3));
        assertEquals(true, taskManager.getValueAt(0, 4));
    }

    
    //Test operations on an empty task list
    @Test
    public void testEmptyTaskList() {
        assertEquals(0, taskManager.getRowCount());
        taskManager.removeTask(0); // Attempt to remove a non-existent task
        assertEquals(0, taskManager.getRowCount()); // Task list remains unchanged
    }

    
    //Add a large number of tasks and check performance
    @Test
    public void testAddManyTasks() {
        for (int i = 0; i < 1000; i++) {
            taskManager.addTask("Task " + i, "Category " + (i % 5), "High", 2024, 11, (i % 30) + 1);
        }
        assertEquals(1000, taskManager.getRowCount());
    }

    
    //Load tasks from a non-existent file
    @Test
    public void testLoadTasksFromNonexistentFile() {
        String nonexistentFile = "nonexistent_tasks";
        File file = new File(nonexistentFile);

        // Verify the file does not exist
        if (file.exists()) {
            file.delete();
        }
        assertFalse(file.exists());

        taskManager.loadTasks(nonexistentFile);
        assertEquals(0, taskManager.getRowCount()); // Task list remains empty
        new File(nonexistentFile).delete();
    }

    
    //Create a new file and verify that the task list is empty
    @Test
    public void testCreateNewFile() throws IOException {
        String newFileName = "test_new_tasks";
        File file = new File(newFileName);

        // Verify the file does not exist
        if (file.exists()) {
            file.delete();
        }
        assertFalse(file.exists());

        taskManager.saveTasks(newFileName);

        assertTrue(file.exists());

        // Verify the task list is empty
        assertEquals(0, taskManager.getRowCount());

        file.delete();
    }

    // Save and load tasks from a new file
    @Test
    public void testSaveAndLoadNewFile() throws IOException {
        String newFileName = "test_save_load_tasks";
        File file = new File(newFileName);

        // Verify the file does not exist
        if (file.exists()) {
            file.delete();
        }
        assertFalse(file.exists());

        taskManager.saveTasks(newFileName);
        assertTrue(file.exists());

        taskManager.addTask("Task1", "Work", "High", 2024, 11, 30);
        taskManager.saveTasks(newFileName);

        // Load the file into a new TaskManager instance
        TaskManager loadedTaskManager = new TaskManager();
        loadedTaskManager.loadTasks(newFileName);

        assertEquals(1, loadedTaskManager.getRowCount());
        assertEquals("Task1", loadedTaskManager.getValueAt(0, 0));
        assertEquals("Work", loadedTaskManager.getValueAt(0, 1));
        assertEquals("High", loadedTaskManager.getValueAt(0, 2));

        file.delete();
    }
    
    @Test
    public void testGetColumnName() {
        assertEquals("Task", taskManager.getColumnName(0));
        assertEquals("Category", taskManager.getColumnName(1));
        assertEquals("Priority", taskManager.getColumnName(2));
        assertEquals("Due date", taskManager.getColumnName(3));
        assertEquals("Completed", taskManager.getColumnName(4));
    }
    
    @Test
    public void testGetColumnCount() {
        assertEquals(5, taskManager.getColumnCount());
    }

    @Test
    public void testGetColumnClass() {
        assertEquals(String.class, taskManager.getColumnClass(0));
        assertEquals(String.class, taskManager.getColumnClass(1));
        assertEquals(String.class, taskManager.getColumnClass(2));
        assertEquals(LocalDate.class, taskManager.getColumnClass(3));
        assertEquals(Boolean.class, taskManager.getColumnClass(4));
    }

    @Test
    public void testIsCellEditable() {
        // Only the "Completed" column (index 4) should be editable
        assertFalse(taskManager.isCellEditable(0, 0));
        assertFalse(taskManager.isCellEditable(0, 1));
        assertFalse(taskManager.isCellEditable(0, 2));
        assertFalse(taskManager.isCellEditable(0, 3));
        assertTrue(taskManager.isCellEditable(0, 4));
    }
    
    
}
