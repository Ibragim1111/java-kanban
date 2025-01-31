package tasks;

import being.taskstypes.TaskType;
import com.example.status.Status;

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
        this.epicId = epicId; // убедитесь в наличии пробелов вокруг '='
    }


    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicID() {
        return epicId;
    }
    @Override
<<<<<<< HEAD
    public TaskType getType() {
=======
    public TaskType getType(){
>>>>>>> f9879962fbe59af4b84ee9e0426d94c4071664de
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return super.toString() + epicId;
    }


    public static SubTask fromString(String[] value) {


        return new SubTask(Integer.parseInt(value[0]),TaskType.valueOf(value[1]), value[2], value[4], Status.valueOf(value[3]), Integer.parseInt(value[5]));
    }


}