package tasks;

import com.example.status.Status;

import java.util.Objects;
import being.taskstypes.TaskType;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;
    private TaskType taskType;
    private Duration duration; // Новое поле
    private LocalDateTime startTime;

    public Task() {
        // Конструктор по умолчанию
    }

    // Конструктор копирования
    public Task(Task task) {
        this.id = task.id;
        this.name = task.name;
        this.description = task.description;
        this.status = task.status;
        this.taskType = task.taskType;
    }

    public Task(int id, TaskType type, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType tasktype) {
        this.taskType = tasktype;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    @Override
    public String toString() {

        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description + "," +
                (startTime != null ? startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : "") + "," +
                (duration != null ? duration.toMinutes() : "");
    }

    public static Task fromString(String[] value) {
        Task task = new Task(
                Integer.parseInt(value[0]),
                TaskType.valueOf(value[1]),
                value[2],
                value[4],
                Status.valueOf(value[3])
        );
        return getTime(value, task);
    }

    protected static Task getTime(String[] value, Task task) {
        if (value.length > 6 && !value[6].isEmpty()) {
            task.setStartTime(LocalDateTime.parse(value[6], DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        }
        if (value.length > 7 && !value[7].isEmpty()) {
            task.setDuration(Duration.ofMinutes(Long.parseLong(value[6])));
        }
        return task;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Task task = (Task) obj;

        return task.id == this.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}



