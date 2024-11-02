import com.sun.security.jgss.InquireType;

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
        // 1. Проверка на самосравнение
        if (this == obj) return true;

        // 2. Проверка типа
        if (obj == null || getClass() != obj.getClass()) return false;

        // 3. Приведение типов
        Task task = (Task) obj;

        // 4. Сравнение полей
        return task.id == this.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class Epic extends Task {

    private final List<Integer> subTasks=new ArrayList<>();

    public List<Integer> SubTask(){
        return subTasks;
    }

    public void addId(Integer id){
        subTasks.add(id);
    }
    public void deleteAllSubTask(){
        subTasks.clear();
    }


}

class SubTask extends Task {

    private int epicId;

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicID() {
        return epicId;
    }



}
