package taskmanager;


import being.taskstypes.TaskType;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import java.nio.charset.StandardCharsets;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.io.File;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
        loadFromFile(file);
    }

    public class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(String message, Throwable cause) {
            super(message, cause);
        }

        public ManagerSaveException(String message) {
            super(message);
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,epic"); // Заголовок CSV
            writer.newLine(); // Переход на новую строку

            for (Task task : getAllTasks()) {
                writer.write(toString(task)); // Исправлено: метод назван с маленькой буквы
                writer.newLine(); // Переход на новую строку после каждой записи
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач в файл: " + file.getAbsolutePath(), e);
        }
    }

    private String toString(Task task) { // Исправлено: метод назван с маленькой буквы
        return task.toString();
    }

    private void loadFromFile(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());

            if (lines.size() <= 1) {

                return; // Или выбросить исключение
            }

            List<Epic> epicsToCreate = new ArrayList<>();
            List<SubTask> subTasksToCreate = new ArrayList<>();

            // Обрабатываем строки файла за один проход
            for (String line : lines.subList(1, lines.size())) {
                Task task = fromString(line);
                if (task == null) continue; // Пропускаем пустые задачи

                if (task.getType() == TaskType.EPIC) {
                    epicsToCreate.add((Epic) task);
                } else if (task.getType() == TaskType.SUBTASK) {
                    subTasksToCreate.add((SubTask) task);
                } else {
                    createTask(task); // Создаем обычные задачи сразу
                }
            }

            // Создаем эпики и подзадачи
            epicsToCreate.forEach(epic -> createEpic(epic, epic.getId()));
            subTasksToCreate.forEach(this::createSubTask);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке задач из файла: " + file.getAbsolutePath(), e);
        }
    }


    // Метод для создания задачи из строки
    public static Task fromString(String value) {
        String[] valueList = value.split(",");

        String type = valueList[1];

        switch (type) {
            case "TASK":
                return Task.fromString(valueList);
            case "SUBTASK":
                return SubTask.fromString(valueList);
            case "EPIC":
                return Epic.fromString(valueList);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type); // Исправлено на русский язык
        }
    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();


    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }


    @Override
    public void createEpic(Epic epic, int id) {
        super.createEpic(epic,id);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();

    }

    @Override
    public void clearEpics() {

        super.clearEpics();
        save();
    }


    @Override
    public void updateTasks(Task task) {
        super.updateTasks(task);
        save();
    }

    @Override
    public void updateSubTasks(SubTask subTask) {
        super.updateTasks(subTask);
        save();
    }

    @Override
    public void updatedEpic(Epic newEpic) {
        super.updatedEpic(newEpic);
        save();
    }


}

