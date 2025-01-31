package taskmanager;

import manager.Managers;
import tasks.*;
import com.example.status.Status;
import historymanager.*;
import java.util.List;
import java.util.ArrayList;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 1; // Счетчик для уникальных идентификаторов
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final HistoryManager history = Managers.getDefaultHistory();


    @Override
    public void createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Tasks.Task cannot be null");
        }
        task.setId(idCounter++);
        task.setStatus(Status.NEW);
        tasks.put(task.getId(), new Task(task));

    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Tasks.SubTask cannot be null");
        }
        if (!epics.containsKey(subTask.getEpicID())) {
            throw new IllegalArgumentException("Tasks.Epic with ID " + subTask.getEpicID() + " does not exist");
        }


        subTask.setId(idCounter++);
        subTasks.put(subTask.getId(), new SubTask(subTask));

        epics.get(subTask.getEpicID()).addId(subTask.getId());
        updateEpicStatus(epics.get(subTask.getEpicID()));


    }
    @Override
    public void createEpic(Epic epic) {
        createEpic(epic, 0);
    }

    @Override
    public void createEpic(Epic epic, int id) {
        if (epic == null) {
            throw new IllegalArgumentException("Tasks.Epic cannot be null");
        }
        int newTaskId = id==0? idCounter++: id;
        epic.setId(newTaskId);
        epics.put(epic.getId(), new Epic(epic));


    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            throw new IllegalArgumentException("Tasks.SubTask with ID " + id + " does not exist");
        }
        int epicId = subTask.getEpicID();
        subTasks.remove(id);
        updateTasks(epics.get(epicId));
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new IllegalArgumentException("Tasks.Epic with ID " + id + " does not exist");
        }
        epic.deleteSubTask();
        epics.remove(id);
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearSubTasks() {
        for (SubTask subtask : subTasks.values()) {
            getEpic(subtask.getEpicID()).deleteSubTask();
            updateEpicStatus(getEpic(subtask.getEpicID()));
        }

    }

    @Override
    public void clearEpics() {

        epics.clear();
        subTasks.clear();
    }

    @Override
    public List<Task> taskListGet() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> subTaskListGet() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTask(int id) {

        history.add(tasks.get(id));

        return tasks.get(id);

    }

    @Override
    public SubTask getSubTasks(int id) {


        history.add(subTasks.get(id));

        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {

        history.add(epics.get(id));

        return epics.get(id);
    }

    @Override
    public void updateTasks(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Tasks.Task cannot be null");
        }
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);

        } else {
            throw new IllegalArgumentException("No task found with ID " + task.getId());
        }
    }

    @Override
    public void updateSubTasks(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Tasks.SubTask cannot be null");
        }

        if (!subTasks.containsKey(subTask.getId())) {
            throw new IllegalArgumentException("No Tasks.SubTask found with ID " + subTask.getId());
        }
        subTasks.put(subTask.getId(), subTask);


        updateEpicStatus(epics.get(subTask.getEpicID()));
    }

    @Override
    public void updatedEpic(Epic newEpic) {
        if (newEpic == null) {
            throw new IllegalArgumentException("Tasks.Epic cannot be null");
        }

        epics.put(newEpic.getId(), newEpic);
    }


    @Override
    public List<SubTask> getSubTaskByEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            throw new IllegalArgumentException("Tasks.Epic with ID " + epicId + " does not exist");
        }
        List<SubTask> newSubTasks = new ArrayList<>();
        List<Integer> subTasksId = epics.get(epicId).subTask();
        for (int subTaskId : subTasksId) {
            newSubTasks.add(subTasks.get(subTaskId));
        }
        return newSubTasks;
    }

    public void addToHistory(Task task) {
        history.add(task);
    }

    public List<Task> getHistory() {
        return history.getHistory();
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.subTask().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;

        for (SubTask subtask : getSubTaskByEpic(epic.getId())) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.NEW);
        }

    }
    public List<Task> getAllTasks() {

            List<Task> allTasksList = new ArrayList<>();

            // Добавляем задачи из первой карты
            allTasksList.addAll(tasks.values());
        allTasksList.addAll(epics.values());
            // Добавляем задачи из второй карты
            allTasksList.addAll(subTasks.values());

            // Добавляем задачи из третьей карты


            return allTasksList;


    }

}
