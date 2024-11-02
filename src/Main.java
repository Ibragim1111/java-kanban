import java.util.List;
public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создание эпиков
        Epic epic1 = new Epic();

        taskManager.createTask(epic1);

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


        // Обновление статуса подзадачи
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubTasks(subtask1);

        // Обновление статуса эпика после изменения подзадачи
        taskManager.updatedEpic(epic1);

        // Вывод статуса эпика после обновления подзадачи
        System.out.println("nUpdated Epic 1 Status: " + epic1.getStatus());

        // Удаление подзадачи
        taskManager.deleteSubTask(subtask3.getId());



    }
}
