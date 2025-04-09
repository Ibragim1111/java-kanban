package HistoryManager;

import org.junit.jupiter.api.Test;
import tasks.Task;
import being.taskstypes.TaskType;
import com.example.status.Status;

import historymanager.InMemoryHistoryManager;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    @Test
    public void testEmptyHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void testDuplicateHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task(1, TaskType.TASK, "Task 1", "Description", Status.NEW);

        historyManager.add(task);
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    public void testRemoveFromHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, TaskType.TASK, "Task 1", "Description", Status.NEW);
        Task task2 = new Task(2, TaskType.TASK, "Task 2", "Description", Status.NEW);
        Task task3 = new Task(3, TaskType.TASK, "Task 3", "Description", Status.NEW);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(2); // Удаляем задачу из середины

        assertEquals(2, historyManager.getHistory().size());
        assertFalse(historyManager.getHistory().contains(task2));
    }
}
