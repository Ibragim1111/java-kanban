import Tasks.*;

import com.example.status.Status;
import manager.Managers;
import managerTask.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Создание эпиков
        Epic epic1 = new Epic();

        taskManager.createEpic(epic1);

        Epic epic2 = new Epic();

        taskManager.createTask(epic2);

        // Создание подзадач
        SubTask subtask1 = new SubTask();

        subtask1.setEpicId(epic1.getId());
        subtask1.setStatus(Status.NEW);
        taskManager.createTask(subtask1);

        SubTask subtask2 = new SubTask();

        subtask2.setEpicId(epic1.getId());
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.createTask(subtask2);

        SubTask subtask3 = new SubTask();

        subtask3.setEpicId(epic2.getId());
        subtask3.setStatus(Status.DONE);
        taskManager.createTask(subtask3);

        // Вывод всех задач
        System.out.println("All Tasks:");
        for (Task task : taskManager.taskListGet()) {
            System.out.println(task.getId() + ": " + task.hashCode() + " - " + task.getStatus());
        }
        for (Task g : taskManager.taskListGet()) {
            taskManager.getTask(g.getId());
        }
        for (Task g : taskManager.subTaskListGet()) {
            taskManager.getSubTasks(g.getId());
        }
        for (Task g : taskManager.EpicListGet()) {
            taskManager.getEpic(g.getId());
        }
        printAllTasks(taskManager);
        // Обновление статуса подзадачи
        subtask1.setStatus(Status.DONE);


    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.taskListGet()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.EpicListGet()) {
            System.out.println(epic);

            for (Task task : manager.getSubTaskByEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.subTaskListGet()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
