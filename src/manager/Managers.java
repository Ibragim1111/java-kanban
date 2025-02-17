package manager;

import taskmanager.*;

import historymanager.*;

public class Managers {
    public static TaskManager getDefault() {
        // Возвращаем стандартную реализацию TaskManager
        return new InMemoryTaskManager(); // или другая реализация
    }

    public static HistoryManager getDefaultHistory() {
        // Возвращаем стандартную реализацию TaskManager
        return new InMemoryHistoryManager(); // или другая реализация
    }

}