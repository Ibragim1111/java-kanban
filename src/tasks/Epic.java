package tasks;

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

    public List<Integer> subTask() {
        return subTasks;
    }

    public void addId(Integer id) {
        subTasks.add(id);
    }

    public void deleteSubTask() {
        subTasks.clear();
    }
}