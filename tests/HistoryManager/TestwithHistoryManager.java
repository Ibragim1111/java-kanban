package HistoryManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Tasks.Epic;
import Tasks.Task;

import java.util.List;

import historyManager.InMemoryHistoryManager;

import static org.junit.jupiter.api.Assertions.*;

public class TestwithHistoryManager {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testAddTask() {
        Task task1 = new Task();
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }

    @Test
    void testRemoveTask() {
        Task task1 = new Task();
        task1.setId(1);
        Task task2 = new Task();
        task2.setId(2);
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    void testUpdateTask() {
        Task task1 = new Task();
        historyManager.add(task1);

        // Изменяем поле задачи
        task1.setName("Updated Task");

        List<Task> history = historyManager.getHistory();
        assertEquals("Updated Task", history.get(0).getName());
    }

    @Test
    void testDuplicateTask() {
        Task task1 = new Task();
        // Задача с тем же ID

        historyManager.add(task1);
        historyManager.add(task1); // Это должно заменить task1

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }
}
