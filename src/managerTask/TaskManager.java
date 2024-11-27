package managerTask;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.List;

public interface TaskManager {
     void createTask(Task task);

     void createSubTask(SubTask subTask);

     void createEpic(Epic epic);

     void deleteTask(int id);

     void deleteSubTask(int id);

     void deleteEpic(int id);

     void clearTasks();

     void clearSubTasks();

     void clearEpics();

     List<Task> taskListGet();

     List<SubTask> subTaskListGet();

     List<Epic> EpicListGet();

     Task getTask(int id);

     SubTask getSubTasks(int id);

     Epic getEpic(int id);

     void updateTasks(Task task);

     void updateSubTasks(SubTask subTask);

     void updatedEpic(Epic newEpic);

     List<SubTask> getSubTaskByEpic(int epicId);

     List<Task> getHistory();
}
