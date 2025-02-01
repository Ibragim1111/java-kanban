package FileBackedTaskManager;
import being.taskstypes.TaskType;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import taskmanager.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.status.Status;
import java.util.List;
import java.util.ArrayList;
import tasks.Task;
import java.nio.file.Files;
import java.io.FileWriter;

import java.io.BufferedWriter;

public class TestwithFileBacked {

    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Создаем временный файл для тестов
        tempFile = File.createTempFile("task_manager", ".csv");
        // Удаляем файл при завершении теста
        tempFile.deleteOnExit();
        manager = new FileBackedTaskManager(tempFile);
    }

    @Test
    public void testSaveAndLoadEmptyFile() {
        // Проверяем, что после загрузки пустого файла нет задач
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void testSaveMultipleTasks() {
        // Создаем несколько задач
        Task task1 = new Task(1, TaskType.TASK, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(2, TaskType.TASK, "Task 2", "Description 2", Status.IN_PROGRESS);

        manager.createTask(task1);
        manager.createTask(task2);

        // Создаем новый менеджер для загрузки из файла
        FileBackedTaskManager newManager = new FileBackedTaskManager(tempFile);

        // Проверяем, что задачи загружены корректно
        assertEquals(2, newManager.getAllTasks().size());
        assertEquals(task1.getName(), newManager.getAllTasks().get(0).getName());
        assertEquals(task2.getName(), newManager.getAllTasks().get(1).getName());
    }

    @Test
    public void testLoadMultipleTasks() {
        // Сохраняем несколько задач в файл напрямую
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();
            writer.write("1,EPIC,Epic 1,NEW,Description 1,0"); // Добавляем эпик
            writer.newLine();
            writer.write("2,TASK,Task 2,IN_PROGRESS,Description 2,0");
            writer.newLine();
            writer.write("3,SUBTASK,SubTask 1,NEW,Description 3,1"); // Подзадача ссылается на эпик
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Проверяем содержимое файла
        try {
            List<String> lines = Files.readAllLines(tempFile.toPath());
            // System.out.print(lines.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileBackedTaskManager newManager = new FileBackedTaskManager(tempFile);


        // Проверяем, что задачи загружены корректно
        assertEquals(3, newManager.getAllTasks().size());


        assertEquals("Task 2", newManager.getAllTasks().get(0).getName()); // Проверка на эпик
        assertEquals("Epic 1", newManager.getAllTasks().get(1).getName()); // Обратите внимание на порядок
    }


}
