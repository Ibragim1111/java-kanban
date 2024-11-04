import Tasks.*;
import com.example.Status.Status;

import java.util.*;

public class TaskManager {
    private int idCounter = 1; // Счетчик для уникальных идентификаторов
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();


    public void createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Tasks.Task cannot be null");
        }
        task.setId(idCounter++);
        task.setStatus(Status.NEW);
        tasks.put(task.getId(), task);
    }

    public void createSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Tasks.SubTask cannot be null");
        }
        if (!epics.containsKey(subTask.getEpicID())) {
            throw new IllegalArgumentException("Tasks.Epic with ID " + subTask.getEpicID() + " does not exist");
        }


            subTask.setId(idCounter++);
            subTasks.put(subTask.getId(), subTask);

            epics.get(subTask.getEpicID()).addId(subTask.getId());
            updateTasks(epics.get(subTask.getEpicID()));


    }

    public void createEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Tasks.Epic cannot be null");
        }
        int newTaskId=idCounter++;
        epic.setId(newTaskId);
        epics.put( epic.getId(),epic);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            throw new IllegalArgumentException("Tasks.SubTask with ID " + id + " does not exist");
        }
        int epicId =subTask.getEpicID();
        subTasks.remove(id);
        updateTasks(epics.get(epicId));
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new IllegalArgumentException("Tasks.Epic with ID " + id + " does not exist");
        }
        epic.deleteSubTask();
        epics.remove(id);
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearSubTasks() {
        for(SubTask subtask : subTasks.values()){
           getEpic(subtask.getEpicID()).deleteSubTask();
           updateEpicStatus(getEpic(subtask.getEpicID()));
        }

    }

    public void сlearEpics() {
        epics.clear();
        subTasks.clear();
    }

    public List<Task> taskListGet() {
         return new ArrayList<>(tasks.values());
    }

    public List<SubTask> subTaskListGet() {
        return new ArrayList<>(subTasks.values());
    }

    public List<Epic> EpicListGet() {
        return new ArrayList<>(epics.values());
    }

    public Task getTask(int id) {
       return tasks.get(id);
    }

    public SubTask getSubTasks(int id) {
        return subTasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void updateTasks(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Tasks.Task cannot be null");
        }
        if(tasks.containsKey(task.getId())){
            tasks.put(task.getId(), task);

        } else {
            throw new IllegalArgumentException("No task found with ID " + task.getId());
         }
    }

    public void updateSubTasks(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Tasks.SubTask cannot be null");
        }

        if (!subTasks.containsKey(subTask.getId())) {
            throw new IllegalArgumentException("No Tasks.SubTask found with ID " + subTask.getId());
        }
        subTasks.put(subTask.getId(),subTask);


        updateEpicStatus(epics.get(subTask.getEpicID()));
    }

    public void updatedEpic(Epic newEpic) {
        if (newEpic == null) {
            throw new IllegalArgumentException("Tasks.Epic cannot be null");
        }

        epics.put(newEpic.getId(),newEpic);
    }



    public List<SubTask> getSubTaskByEpic(int epicId){
        Epic epic = epics.get(epicId);
        if (epic == null) {
            throw new IllegalArgumentException("Tasks.Epic with ID " + epicId + " does not exist");
        }
     List<SubTask> newSubTasks=new ArrayList<>();
     List<Integer> subTasksId =epics.get(epicId).SubTask();
     for(int subTaskId : subTasksId){
         newSubTasks.add(subTasks.get(subTaskId));
     }
     return newSubTasks;
    }


    private void updateEpicStatus(Epic epic) {
        if (epic.SubTask().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;

        for (SubTask subtask : getSubTaskByEpic(epic.getId())) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
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

}
