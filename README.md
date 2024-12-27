# Personal-To-Do-List-Manager
The Personal To-Do List Manager is a Java-based desktop application designed to help users  manage their tasks effectively. This application enables users to add, view, categorize, sort,  and track their tasks in a simple graphical interface using Swing components.

## Features

### Task Management
- **Add Tasks**: Input task descriptions, assign priorities (High, Medium, Low), and add due dates.
- **Delete Tasks**: Remove tasks that are no longer relevant or completed.
- **View Tasks**: Display all tasks in an organized list.
- **Sort Tasks**: Sort tasks by priority or due date.
- **Categorize Tasks**: Group tasks into categories such as "Work", "Personal", or "Other".

### Graphical User Interface
- Built using Swing components like `JTable` and `JComboBox` for an interactive and intuitive experience.

## How It Works

1. **Load Tasks**: Load tasks from a file or create a new file if none exists.
2. **Add Tasks**: Add new tasks by specifying details like name, priority, category, and due date.
3. **Delete Tasks**: Select a task from the list and remove it.
4. **View and Sort Tasks**: View tasks, sort them by priority or due date, and filter by category.


## Requirements

- **Java Version**: 21 or higher
- **Maven**: Used for dependency management and project build.

## How to Run
Build the project using Maven:
```
mvn compile
```

```
mvn package
```
Run the application:
```
java -jar target/ToDoListManager-1.0-SNAPSHOT.jar
```


## Technical Details

### Frameworks and Tools
- **Java Swing**: For the graphical user interface.
- **Java Serialization**: Handles saving and loading task data.
- **JUnit**: Provides unit testing for key functionalities like task addition, removal, and file operations.

### Core Classes
1. **`Task`**: Encapsulates task details (description, priority, category, due date, and completion status). Implements `Serializable` for data persistence.
2. **`TaskManager`**: Manages the collection of tasks, including adding, deleting, saving, and loading tasks. Extends `AbstractTableModel` for interaction with `JTable`.
3. **`TaskManagerFrame`**: Handles the GUI and user actions, integrating with the `TaskManager` class for task operations.

## Testing

The application includes extensive unit tests using JUnit:
- **Task Operations**: Adding, removing, and editing tasks.
- **Data Persistence**: Saving tasks to a file and reloading them.
- **Edge Cases**: Tests behavior with empty task lists and nonexistent files.
