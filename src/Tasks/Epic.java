package Tasks;
import java.util.List;
import java.util.ArrayList;
public class Epic extends Task {

    private final List<Integer> subTasks=new ArrayList<>();

    public List<Integer> SubTask(){
        return subTasks;
    }

    public void addId(Integer id){
        subTasks.add(id);
    }
    public void deleteSubTask(){
        subTasks.clear();
    }


}
