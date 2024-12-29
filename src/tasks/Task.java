package tasks;

import com.example.status.Status;

import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;

    public Task() {

    }

    // Getters and Setters
    public Task(Task task) {
        this.id = task.id;
        this.name = task.name;
        this.description = task.description;
        this.status = task.status;
    }

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




