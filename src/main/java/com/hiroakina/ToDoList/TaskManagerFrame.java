package com.hiroakina.ToDoList;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.time.*;
import java.util.*;
import java.awt.event.ActionEvent;
import javax.swing.table.*;

public class TaskManagerFrame extends JFrame{

	private TaskManager taskManager;
	private JTable taskTable;
	private String selectedFilePath;
	private JTextField taskField;
	private JTextField categoryField;
	private JTextField yearField;
    private JTextField monthField;
    private JTextField dayField;
	private CardLayout layout;
	private JPanel cardPanel;
	private JComboBox<String> priorityComboBox;
	private JComboBox<String> filterCategoryComboBox;
	private TableRowSorter<TableModel> sorter;
	
	// Constructor to set up the frame
	private TaskManagerFrame() {
		super("To-Do List Application");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(600,400);
		
		taskManager = new TaskManager();
		taskTable = new JTable(taskManager);
		taskTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(taskTable);
		//// Set up sorter with custom comparator for priority
		sorter = new TableRowSorter<>(taskManager);
		sorter.setComparator(2, new PriorityComparator()); // Set custom comparator for the priority column (index 2)
        taskTable.setRowSorter(null);
		
		
		
        // Set up card layout for the application
		layout = new CardLayout();
		cardPanel = new JPanel(layout);
		this.add(cardPanel);
		
		// Panel for loading tasks
		JPanel loadPanel = new JPanel();
		JButton createButton = new JButton("Create a file to save tasks");
		JButton loadButton = new JButton("Load Tasks from File");
		loadPanel.add(createButton);
		loadPanel.add(loadButton);
		createButton.addActionListener(new CreateButtonActionListener());
		loadButton.addActionListener(new LoadButtonActionListener());
		cardPanel.add(loadPanel,"LoadPanel");
		
		JMenuItem loadTaskMenuItem = new JMenuItem("Load Task");
		loadTaskMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	switchToOtherPanel("LoadPanel");
                
            }
        });

        
		
		
		// Add Task Panel
		JPanel addPanel = new JPanel();
		addPanel.setLayout(new BorderLayout());
        JPanel enterPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(enterPanel);
        enterPanel.setLayout(groupLayout);
        addPanel.add(enterPanel,BorderLayout.SOUTH);
        addPanel.add(scrollPane, BorderLayout.CENTER);
        

        // Create components for adding tasks
        JLabel taskLabel = new JLabel("Task:");
        JLabel nameLabel = new JLabel("Name");
        taskField = new JTextField(20);
        JLabel categoryLabel = new JLabel("Category");
        categoryField = new JTextField(15);
        JLabel priorityLabel = new JLabel("Priority");
        Vector<String> comboData = new Vector<>();
        comboData.add("High");
        comboData.add("Medium");
        comboData.add("Low");
        priorityComboBox = new JComboBox<>(comboData);

        JLabel dueDateLabel = new JLabel("Due date: ");
        JLabel yearLabel = new JLabel("Year");
        yearField = new JTextField(4);
        JLabel monthLabel = new JLabel("Month");
        monthField = new JTextField(2);
        JLabel dayLabel = new JLabel("Day");
        dayField = new JTextField(2);

        JLabel noteLabel = new JLabel("※ Due date is written in number");

        JButton addButton = new JButton("Add");

        // Set up layout for the "Add Task" panel
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(taskLabel)                  
                    .addComponent(nameLabel)
                    .addComponent(taskField)                 
                    .addComponent(categoryLabel)
                    .addComponent(categoryField)                
                    .addComponent(priorityLabel)
                    .addComponent(priorityComboBox)            
                    .addComponent(addButton))
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(dueDateLabel)                   
                    .addComponent(yearLabel)
                    .addComponent(yearField)               
                    .addComponent(monthLabel)
                    .addComponent(monthField)           
                    .addComponent(dayLabel)
                    .addComponent(dayField))                
                .addComponent(noteLabel)
        );

        groupLayout.setVerticalGroup(
            groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(taskLabel)
                    .addComponent(nameLabel)
                    .addComponent(taskField)
                    .addComponent(categoryLabel)
                    .addComponent(categoryField)
                    .addComponent(priorityLabel)
                    .addComponent(priorityComboBox)
                    .addComponent(addButton))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(dueDateLabel)
                    .addComponent(yearLabel)
                    .addComponent(yearField)
                    .addComponent(monthLabel)
                    .addComponent(monthField)
                    .addComponent(dayLabel)
                    .addComponent(dayField))
                .addComponent(noteLabel)
        );

        // Adding addPanel to cardPanel
        cardPanel.add(addPanel, "AddPanel");
        
        addButton.addActionListener(new AddButtonActionListener());
        


        // Menu bar setup
        JMenuItem addTaskMenuItem = new JMenuItem("Add Task");
        addTaskMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	switchToOtherPanel("AddPanel");
                addPanel.add(scrollPane, BorderLayout.CENTER);
            }
        });
        
        
        // Panel for deleting tasks
        JPanel deletePanel = new JPanel();
		deletePanel.setLayout(new BorderLayout());
        JPanel guidePanel = new JPanel();
        deletePanel.add(guidePanel,BorderLayout.SOUTH);
        deletePanel.add(scrollPane, BorderLayout.CENTER);
        
        JLabel deleteLabel = new JLabel("Choose the row of task to delete and click the delete button");
        JButton deleteButton = new JButton("Delete Selected");
        guidePanel.add(deleteLabel);
        guidePanel.add(deleteButton);
        deleteButton.addActionListener(new DeleteButtonActionListener());
        
        cardPanel.add(deletePanel,"DeletePanel");
        
        JMenuItem deleteTaskMenuItem = new JMenuItem("Delete Task");
        deleteTaskMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	switchToOtherPanel("DeletePanel");
                deletePanel.add(scrollPane, BorderLayout.CENTER);
            }
        });
        
        
        // Panel for viewing tasks
        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BorderLayout());
        JPanel categoryPanel = new JPanel();
        viewPanel.add(categoryPanel,BorderLayout.SOUTH);
        viewPanel.add(scrollPane, BorderLayout.CENTER);
        cardPanel.add(viewPanel, "ViewPanel");
        
        filterCategoryComboBox = new JComboBox<>();
        updateFilterCategoryComboBox();
        filterCategoryComboBox.addActionListener(new FilterCategoryActionListener());
        
        // Add filter options to category panel
        categoryPanel.add(new JLabel("Filter by Category:"));
        categoryPanel.add(filterCategoryComboBox);
        
        // Sort description label
        JLabel sortby = new JLabel("Sort tasks by clicking on the corresponding column header");
        viewPanel.add(sortby,BorderLayout.NORTH);
        
        JMenuItem viewTaskMenuItem = new JMenuItem("View Task");
        viewTaskMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPanel.add(scrollPane, BorderLayout.CENTER);
                switchToOtherPanel("ViewPanel");
                updateFilterCategoryComboBox();                
            }
        });
        
        
        // Menu bar for application
        JMenuBar menuBar =new JMenuBar();
		JMenu menu = new JMenu("Options");
        menu.add(loadTaskMenuItem);
        menu.add(addTaskMenuItem);
        menu.add(deleteTaskMenuItem);
        menu.add(viewTaskMenuItem);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        
        
        // Save data on window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	if (taskManager != null && selectedFilePath != null && !selectedFilePath.isEmpty()) {
                    try {
                        taskManager.saveTasks(selectedFilePath);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                
            }
        });
        
	}
	
	
	
	
	// ActionListener to create a new file for tasks
	private class CreateButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
            JFrame createFileFrame = new JFrame("Create New Task File");
            createFileFrame.setSize(300, 150);
            createFileFrame.setLayout(new BorderLayout());

            JPanel inputPanel = new JPanel();
            JLabel fileNameLabel = new JLabel("Enter file name: ");
            JTextField fileNameField = new JTextField(15);
            inputPanel.add(fileNameLabel);
            inputPanel.add(fileNameField);

            JButton createFileButton = new JButton("Create File");
            createFileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    String fileName = fileNameField.getText();
                    if (!fileName.isEmpty()) {
                    	File newFile = new File(fileName);
                    	// Check if file already exists
                    	if (newFile.exists()) {
                            JOptionPane.showMessageDialog(createFileFrame, "The file name already exists. Please enter a new file name.", "Error", JOptionPane.ERROR_MESSAGE);
                            return; // Prompt for re-entering the file name
                        }
                        int confirm = JOptionPane.showConfirmDialog(createFileFrame, "Create file " + fileName + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                        	
                        	// Save the current tasks to the file if needed
                        	if (selectedFilePath != null && !selectedFilePath.isEmpty()) {
                                File existingFile = new File(selectedFilePath);
                                if (existingFile.exists() && existingFile.canWrite()) {
                                    taskManager.saveTasks(selectedFilePath);
                                 // Reset the task list
                                    taskManager = new TaskManager();
                                    taskTable.setModel(taskManager);
                                    sorter = new TableRowSorter<>(taskManager);
                                    sorter.setComparator(2, new PriorityComparator());
                                    taskTable.setRowSorter(null);
                                    
                                } else {
                                    JOptionPane.showMessageDialog(createFileFrame, "Unable to save to the current file. Please check permissions.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        	
                        	
                        	selectedFilePath = fileName;
                            taskManager.saveTasks(fileName);
                            JOptionPane.showMessageDialog(createFileFrame, "File created successfully: " + fileName);
                            createFileFrame.dispose();
                            layout.show(cardPanel, "LoadPanel");
                        }
                    } else {
                        JOptionPane.showMessageDialog(createFileFrame, "File name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            createFileFrame.add(inputPanel, BorderLayout.CENTER);
            createFileFrame.add(createFileButton, BorderLayout.SOUTH);
            createFileFrame.setVisible(true);
		}
	}
	
	// ActionListener to load tasks from an existing file
	private class LoadButtonActionListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	 JFileChooser fileChooser = new JFileChooser();
	         int returnValue = fileChooser.showOpenDialog(TaskManagerFrame.this);

	         if (returnValue == JFileChooser.APPROVE_OPTION) {
	        	 
	        	// Save the current state if needed
	             if (selectedFilePath != null && !selectedFilePath.isEmpty()) {
	                 File existingFile = new File(selectedFilePath);
	                 if (existingFile.exists() && existingFile.canWrite()) {
	                     taskManager.saveTasks(selectedFilePath);
	                     // Reset the task list
	    	             taskManager = new TaskManager();
	    	             taskTable.setModel(taskManager);
	    	             sorter = new TableRowSorter<>(taskManager);
	    	             sorter.setComparator(2, new PriorityComparator());
	    	             taskTable.setRowSorter(null);
	                 } else {
	                     JOptionPane.showMessageDialog(TaskManagerFrame.this, "Unable to save to the current file. Please check permissions.", "Error", JOptionPane.ERROR_MESSAGE);
	                 }
	             }
	        	 
	             selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
	             
	          
	             
	             boolean isSuccess = taskManager.loadTasks(selectedFilePath);
	             if (isSuccess) {
	                 JOptionPane.showMessageDialog(TaskManagerFrame.this, "Tasks successfully loaded from: " + selectedFilePath);
	             } else {
	                 JOptionPane.showMessageDialog(TaskManagerFrame.this, "Failed to load the selected file. Please choose a valid file.", "Error", JOptionPane.ERROR_MESSAGE);
	             }
	         }
	     }
	}
	
	// ActionListener to add a new task
	private class AddButtonActionListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {

	        if (selectedFilePath == null) {
	            JOptionPane.showMessageDialog(TaskManagerFrame.this, "Please load a file before adding tasks.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // Check user inputs
	        String task = taskField.getText().trim();
	        String category = categoryField.getText().trim();
	        String yearStr = yearField.getText().trim();
	        String monthStr = monthField.getText().trim();
	        String dayStr = dayField.getText().trim();

	        if (task.isEmpty() || category.isEmpty() || yearStr.isEmpty() || monthStr.isEmpty() || dayStr.isEmpty()) {
	            JOptionPane.showMessageDialog(TaskManagerFrame.this, "All information of the task should be entered.", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        try {
	            String priority = priorityComboBox.getSelectedItem().toString();
	            int year = Integer.parseInt(yearStr);
	            int month = Integer.parseInt(monthStr);
	            int day = Integer.parseInt(dayStr);

	            // Validate date
	            LocalDate dueDate;
	            try {
	                dueDate = LocalDate.of(year, month, day);
	            } catch (DateTimeException dateEx) {
	                JOptionPane.showMessageDialog(TaskManagerFrame.this, "Invalid date entered. Please enter a valid year, month, and day.", "Error", JOptionPane.ERROR_MESSAGE);
	                return;
	            }

	            // Add the task and save to file
	            taskManager.addTask(task, category, priority, year, month, day);
	            taskManager.saveTasks(selectedFilePath);

	            // Clear fields
	            taskField.setText("");
	            categoryField.setText("");
	            yearField.setText("");
	            monthField.setText("");
	            dayField.setText("");

   	            // Update filter category combo box
	            updateFilterCategoryComboBox();

	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(TaskManagerFrame.this, "Year, Month, and Day must be numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
	        } catch (IllegalArgumentException ex) {
	            JOptionPane.showMessageDialog(TaskManagerFrame.this, "Invalid date entered. Please check the year, month, and day.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
	
	// ActionListener to delete a selected task
    private class DeleteButtonActionListener implements ActionListener {
        

        @Override
        public void actionPerformed(ActionEvent e) {
        	
        	if (selectedFilePath == null) {
                JOptionPane.showMessageDialog(TaskManagerFrame.this, "Please load a file before deleting tasks.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        	
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow >= 0) {
                taskManager.removeTask(selectedRow);
                taskManager.saveTasks(selectedFilePath);
                
                updateFilterCategoryComboBox();
            } else {
                JOptionPane.showMessageDialog(TaskManagerFrame.this, "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Update the filter combo box with the list of categories
    private void updateFilterCategoryComboBox() {
        filterCategoryComboBox.removeAllItems();
        filterCategoryComboBox.addItem("All");
        Set<String> categories = taskManager.getCategories();
        for (String category : categories) {
            filterCategoryComboBox.addItem(category);
        }
    }
    // ActionListener to filter tasks by selected category
    private class FilterCategoryActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedCategory = (String) filterCategoryComboBox.getSelectedItem();
            
            // Do nothing if selected category is null
            if (selectedCategory == null) {
                return;
            }

            if (selectedCategory.equals("All")) {
                sorter.setRowFilter(null); 
            } else {
            	sorter.setRowFilter(RowFilter.regexFilter(selectedCategory, 1)); // Filter by category column
            }
        }
    }
    
 // Method to switch between different panels
    private void switchToOtherPanel(String panelName) {
    	if (!panelName.equals("ViewPanel")) {
            taskTable.setRowSorter(null);
        } else {
            taskTable.setRowSorter(sorter);
            taskManager.fireTableDataChanged();
        }
        layout.show(cardPanel, panelName);
    }
    
    // Custom comparator to sort priorities: High, Medium, Low
    public class PriorityComparator implements Comparator<String> {
        private final ArrayList<String> priorityOrder = new ArrayList<>();

        public PriorityComparator() {
            priorityOrder.add("High");
            priorityOrder.add("Medium");
            priorityOrder.add("Low");
        }

        @Override
        public int compare(String p1, String p2) {
            return Integer.compare(priorityOrder.indexOf(p1), priorityOrder.indexOf(p2));
        }
    }

    
	public static void main(String[] args) {
		TaskManagerFrame tmf = new TaskManagerFrame();
		tmf.setVisible(true);
	}
	
}

