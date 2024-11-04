package Tasks;
import com.example.Status.Status;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;

    // Getters and Setters

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

    public void setName(Status status) {
        this.status = status;
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




