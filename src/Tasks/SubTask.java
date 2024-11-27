package Tasks;

import java.util.ArrayList;

public class SubTask extends Task {

    private int epicId;
    public SubTask(){

    }

    public SubTask(Task task) {
        super(task);
        if(task instanceof SubTask){
            SubTask newTask=(SubTask) task;
            epicId=newTask.getEpicID();
        }
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicID() {
        return epicId;
    }



}