package taskmanager;

import tasks.Epic;


import tasks.SubTask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
     void createTask(Task task);

     void createSubTask(SubTask subTask);

     void createEpic(Epic epic);

     void createEpic(Epic epic, int id);

     void deleteTask(int id);

     void deleteSubTask(int id);

     void deleteEpic(int id);

     void clearTasks();

     void clearSubTasks();

     void clearEpics();

     List<Task> taskListGet();

     List<SubTask> subTaskListGet();

     List<Epic> getEpicList(); // Исправлено

     Task getTask(int id);

     SubTask getSubTasks(int id);

     Epic getEpic(int id);

     void updateTasks(Task task);

     void updateSubTasks(SubTask subTask);

     void updatedEpic(Epic newEpic);

     List<SubTask> getSubTaskByEpic(int epicId);

     List<Task> getHistory();

     public List<Task> getAllTasks();
}