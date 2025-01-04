package historymanager;

import java.util.List;

import tasks.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private final DoublyLinkedList history; //Для удаления дублткатов

    public InMemoryHistoryManager() {
        this.history = new DoublyLinkedList();
    }

    @Override
    public void add(Task task) {
        history.add(task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(id);
    }


    @Override
    public List<Task> getHistory() {
        return history.getTasks(); // Возвращаем историю как список
    }
}

