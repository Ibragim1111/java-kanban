package TaskManager;

import taskmanager.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @Override
    protected FileBackedTaskManager createTaskManager() {
        try {
            return new FileBackedTaskManager(File.createTempFile("task_manager", ".csv"));
        } catch (IOException io) {
            System.out.println("Ошибка с файлом: " + io.getMessage());
        }
        return null;
    }
}
