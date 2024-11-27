package historyManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import Tasks.Task;
public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedHashSet<Task> history; //Для удаления дублткатов

    public InMemoryHistoryManager() {
        this.history = new LinkedHashSet<>();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            history.add(task);
            // Ограничиваем размер истории до 10 задач
            if (history.size() > 10) {
                history.remove(history.iterator().next()); // Удаляем самую старую задачу
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history); // Возвращаем историю как список
    }
}

