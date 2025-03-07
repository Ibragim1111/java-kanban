package tasks;

import being.taskstypes.TaskType;
import com.example.status.Status;

import java.time.LocalDateTime;
import java.util.*;
import java.time.Duration;


public class Epic extends Task {

    private List<Integer> subTasks = new ArrayList<>();

    private Map<Integer, SubTask> subTasksFromManager = new HashMap<>();

    public Epic() {

    }

    public Epic(Task task) {
        super(task);
        if (this.getTaskType() == task.getTaskType()) {
            Epic newTask = (Epic) task;
            subTasks = new ArrayList<>(newTask.subTasks);
        }
    }

    public Epic(int id, TaskType type, String name, String description, Status status) {
        super(id, type, name, description, status);
    }

    public List<Integer> subTask() {
        return subTasks;
    }

    public void addId(Integer id) {
        subTasks.add(id);

    }

    public void deleteSubTask(int id) {
        subTasks.remove(Integer.valueOf(id));

    }

    public void deleteSubTasks() {
        subTasks.clear();
    }

    @Override
    public Duration getDuration() {
        return subTasks.stream()
                .map(subTaskId -> subTasksFromManager.get(subTaskId).getDuration())
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public LocalDateTime getStartTime() {
        return subTasks.stream()
                .map(subTaskId -> subTasksFromManager.get(subTaskId).getStartTime())
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public LocalDateTime getEndTime() {
        return subTasks.stream()
                .map(subTaskId -> subTasksFromManager.get(subTaskId).getEndTime())
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    public void getSubTask(Map<Integer, SubTask> subTasksFromManager) {
        this.subTasksFromManager = subTasksFromManager;
    }

    @Override
    public String toString() {
        return super.toString(); // Эпики не имеют epicId
    }

    public static Epic fromString(String[] value) {
        Epic epic = new Epic(
                Integer.parseInt(value[0]),
                TaskType.valueOf(value[1]),
                value[2],
                value[4],
                Status.valueOf(value[3])
        );
        return (Epic) getTime(value, epic);
    }
}