package tasks;

import being.taskstypes.TaskType;
import com.example.status.Status;

import java.util.List;
import java.util.ArrayList;

public class Epic extends Task {

    private List<Integer> subTasks = new ArrayList<>();

    public Epic() {

    }

    public Epic(Task task) {
        super(task);
        if (task instanceof Epic) {
            Epic newTask = (Epic) task;
            subTasks = new ArrayList<>(newTask.subTasks);
        }
    }

    public Epic(int id, TaskType type, String name, String description, Status status) {
      super(id,type, name, description,status);
    }

    public List<Integer> subTask() {
        return subTasks;
    }

    public void addId(Integer id) {
        subTasks.add(id);
    }

    public void deleteSubTask() {
        subTasks.clear();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return super.toString(); // Эпики не имеют epicId
    }

    public static Epic fromString(String[] value) {


        return new Epic(Integer.parseInt(value[0]),TaskType.valueOf(value[1]), value[2], value[4], Status.valueOf(value[3]));
    }
}