package TaskOverlap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import tasks.Task;
import com.example.status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import taskmanager.*;
import being.taskstypes.TaskType;

public class TestTaskOverlap {
    @Test
    public void testTasksOverlap() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task(1, TaskType.TASK, "Task 1", "Description", Status.NEW);
        task1.setStartTime(LocalDateTime.of(2023, 10, 1, 10, 0));
        task1.setDuration(Duration.ofMinutes(30));

        Task task2 = new Task(2, TaskType.TASK, "Task 2", "Description", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2023, 10, 1, 10, 15));
        task2.setDuration(Duration.ofMinutes(30));

        taskManager.createTask(task1);
        assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2));
    }
}
