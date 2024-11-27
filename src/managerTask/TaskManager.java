package managerTask;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.List;

public interface TaskManager {
    public void createTask(Task task);

    public void createSubTask(SubTask subTask);

    public void createEpic(Epic epic);

    public void deleteTask(int id);

    public void deleteSubTask(int id);

    public void deleteEpic(int id);

    public void clearTasks();

    public void clearSubTasks();

    public void clearEpics();

    public List<Task> taskListGet();

    public List<SubTask> subTaskListGet();

    public List<Epic> EpicListGet();

    public Task getTask(int id);

    public SubTask getSubTasks(int id);

    public Epic getEpic(int id);

    public void updateTasks(Task task);

    public void updateSubTasks(SubTask subTask);

    public void updatedEpic(Epic newEpic);

    public List<SubTask> getSubTaskByEpic(int epicId);

    public List<Task> getHistory();
}
