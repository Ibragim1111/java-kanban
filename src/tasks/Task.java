package tasks;

import com.example.status.Status;
import java.util.Objects;
import being.taskstypes.TaskType;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;

    public Task() {
        // Конструктор по умолчанию
    }

    // Конструктор копирования
    public Task(Task task) {
        this.id = task.id;
        this.name = task.name;
        this.description = task.description;
        this.status = task.status;
    }

    // Конструктор с параметрами
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

    @Override
    public String toString() {
        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description + ",";
    }

    public static Task fromString(String[] value) {
        return new Task(
                Integer.parseInt(value[0]),
                TaskType.valueOf(value[1]),
                value[2],
                value[4],
                Status.valueOf(value[3])
        );
    }
<<<<<<< HEAD
    public TaskType getType() {
        return TaskType.TASK;
    }

=======
    public TaskType getType(){
        return TaskType.TASK;
    }
>>>>>>> f9879962fbe59af4b84ee9e0426d94c4071664de
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



