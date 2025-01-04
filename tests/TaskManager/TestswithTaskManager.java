package TaskManager;

import historyManager.*;
import tasks.Epic;
import tasks.Task;
import taskmanager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestswithTaskManager {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testAddAndFindTasksById() {
        Task task = new Task();
        task.setName("Test Task");
        taskManager.createTask(task);

        Task foundTask = taskManager.getTask(task.getId());
        assertEquals(task.getName(), foundTask.getName());
    }

    @Test
    public void testUniqueIdsForDifferentTypes() {
        Task task = new Task();
        taskManager.createTask(task);

        Epic epic = new Epic();
        taskManager.createEpic(epic);

        assertNotEquals(task.getId(), epic.getId());
    }

    @Test
    public void testTaskImmutabilityOnAdd() {
        Task originalTask = new Task();
        originalTask.setName("Original Task");

        taskManager.createTask(originalTask);

        Task addedTask = taskManager.getTask(originalTask.getId());

        // Изменяем оригинальную задачу
        originalTask.setName("Modified Task");

        // Убеждаемся, что добавленная задача не изменилась
        assertEquals("Original Task", addedTask.getName());
    }

    @Test
    public void testHistoryManagerKeepsPreviousVersion() {
        HistoryManager historyManager = new InMemoryHistoryManager();

        historyManager.add(new Task());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}
