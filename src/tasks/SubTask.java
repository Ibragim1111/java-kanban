package tasks;

import being.taskstypes.TaskType;
import com.example.status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {

    private int epicId;

    public SubTask() {

    }

    public SubTask(Task task) {
        super(task);
        if (task instanceof SubTask) {
            SubTask newTask = (SubTask) task;
            epicId = newTask.getEpicID();
        }
    }

    public SubTask(int id, TaskType type, String name, String description, Status status, int epicId) {
        super(id, type, name, description, status);
        this.epicId = epicId;
    }


    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicID() {
        return epicId;
    }

    @Override
    public String toString() {
        return super.toString() + epicId;
    }

    public static SubTask fromString(String[] value) {
        SubTask subTask = new SubTask(Integer.parseInt(value[0]),TaskType.valueOf(value[1]), value[2], value[4], Status.valueOf(value[3]), Integer.parseInt(value[5]));
        if (value.length > 6 && !value[6].isEmpty()) {
            subTask.setStartTime(LocalDateTime.parse(value[5], DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        }
        if (value.length > 7 && !value[7].isEmpty()) {
            subTask.setDuration(Duration.ofMinutes(Long.parseLong(value[6])));
        }
        return (SubTask) getTime(value, subTask);


    }


}