package TaskManager;

import tasks.Epic;
import tasks.Task;
import tasks.SubTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.TaskManager;
import being.taskstypes.TaskType;
import com.example.status.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    public void testCreateTask() {
        Task task = new Task(1, TaskType.TASK, "Task 1", "Description", Status.NEW);
        taskManager.createTask(task);
        assertEquals(task, taskManager.getTask(1));
    }

    @Test
    public void testCreateSubTask() {
        Epic epic = new Epic(1, TaskType.EPIC, "Epic 1", "Description", Status.NEW);
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask(2, TaskType.SUBTASK, "SubTask 1", "Description", Status.NEW, 1);
        taskManager.createSubTask(subTask);

        assertEquals(subTask, taskManager.getSubTask(2));
    }

    @Test
    public void testCreateEpic() {
        Epic epic = new Epic(1, TaskType.EPIC, "Epic 1", "Description", Status.NEW);
        taskManager.createEpic(epic);

        assertEquals(epic, taskManager.getEpic(1));
    }
}
