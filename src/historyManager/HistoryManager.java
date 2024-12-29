package historyManager;
import java.util.List;
import Tasks.*;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
