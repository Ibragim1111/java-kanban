package TaskException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import taskmanager.InMemoryTaskManager;

public class TaskExceptionTest {
    @Test
    public void testTaskCreationWithNull() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(null));
    }

    @Test
    public void testTaskUpdateWithNull() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        assertThrows(IllegalArgumentException.class, () -> taskManager.updateTasks(null));
    }
}
