package taskmanager;

import manager.Managers;
import tasks.*;
import com.example.status.Status;
import historymanager.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsFirst(Comparator.naturalOrder()))
    );
    private int idCounter = 1; // Счетчик для уникальных идентификаторов
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final HistoryManager history = Managers.getDefaultHistory();
    protected final List<Task> allTasks = new ArrayList<>();

    public boolean isTasksOverlap(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        return !task1.getEndTime().isBefore(task2.getStartTime()) &&
                !task1.getStartTime().isAfter(task2.getEndTime());
    }

    private void addStartTimes(Task task) {
        if (task.getStartTime() != null) {
            boolean hasOverlap = prioritizedTasks.stream()
                    .anyMatch(existingTask -> isTasksOverlap(task, existingTask));
            if (hasOverlap) {
                throw new IllegalArgumentException("Задача пересекается по времени с другой задачей");
            }
            prioritizedTasks.add(task);
        }
    }

    private void makeTimeForEpic(int id) {
        epics.get(id).getSubTask(subTasks);
        addStartTimes(epics.get(id));
    }

    @Override
    public void createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Tasks.Task cannot be null");
        }
        task.setId(idCounter++);
        task.setStatus(Status.NEW);
        tasks.put(task.getId(), new Task(task));
        allTasks.add(task);
        addStartTimes(task);
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
        makeTimeForEpic(subTask.getEpicID());
        allTasks.add(subTask);
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Tasks.Epic cannot be null");
        }
        int newTaskId = idCounter++;
        epic.setId(newTaskId);
        epics.put(epic.getId(), new Epic(epic));
        makeTimeForEpic(epic.getId());
        allTasks.add(epic);
    }

    @Override
    public void deleteTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            tasks.remove(id);
            prioritizedTasks.remove(task);
            allTasks.remove(task);
        }
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            throw new IllegalArgumentException("Tasks.SubTask with ID " + id + " does not exist");
        }
        int epicId = subTask.getEpicID();
        subTasks.remove(id);
        prioritizedTasks.remove(subTask);
        allTasks.remove(subTask);
        updatedEpic(epics.get(epicId));
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new IllegalArgumentException("Tasks.Epic with ID " + id + " does not exist");
        }
        epic.deleteSubTasks();
        prioritizedTasks.remove(epic);
        epics.remove(id);
        allTasks.remove(epic);
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearSubTasks() {
        for (SubTask subtask : subTasks.values()) {
            getEpic(subtask.getEpicID()).deleteSubTasks();
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
        Task task = tasks.get(id);
        if (task != null) {
            history.add(task);
        }
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            history.add(subTask);
        }
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            history.add(epic);
        }
        return epic;
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
        addStartTimes(task);
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
        makeTimeForEpic(subTask.getEpicID());

        updateEpicStatus(epics.get(subTask.getEpicID()));
    }

    @Override
    public void updatedEpic(Epic newEpic) {
        if (newEpic == null) {
            throw new IllegalArgumentException("Tasks.Epic cannot be null");
        }
        makeTimeForEpic(newEpic.getId());
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
        boolean anyInProgress = false;

        for (SubTask subtask : getSubTaskByEpic(epic.getId())) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                anyInProgress = true;
            } else if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (anyInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.NEW);
        }
    }

    public List<Task> getAllTasks() {
        return allTasks;
    }

    public Set<Task> getPrioritized() {
        return prioritizedTasks;
    }
}
