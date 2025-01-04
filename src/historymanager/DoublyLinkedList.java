package historymanager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DoublyLinkedList {
    public Node<Task> head;
    public Node<Task> tail;
    private int size = 0;
    private Map<Integer, Node> taskMap = new HashMap<>();
    // Вложенный класс Node


    // Конструктор
    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;

    }

    // Метод для добавления элемента в конец списка
    private void linkLast(Task task) {


        // Приводим объект к типу Person

        Node<Task> newNode = new Node<Task>(task);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        taskMap.put(task.getId(), newNode);
    }

    public void add(Task task) {
        // Если задача уже существует, удаляем её
        if (taskMap.containsKey(task.getId())) {
            removeNode(task.getId());
        }

        // Создаем новый узел
        Node newNode = new Node(task);

        // Добавляем новый узел в конец списка
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        // Обновляем размер списка
        size++;

        // Сохраняем узел в HashMap
        taskMap.put(task.getId(), newNode);
    }

    // Метод для удаления узла
    public void removeNode(int taskId) {
        Node nodeToRemove = taskMap.get(taskId);

        if (nodeToRemove.data instanceof Epic) {
            Epic epc = (Epic) nodeToRemove.data;
            List<Integer> subTaskIds = epc.subTask(); // Предполагаем, что есть метод для получения ID подзадач

            // Удаляем все подзадачи из истории
            for (Integer subTaskId : subTaskIds) {
                if (taskMap.get(subTaskId).data instanceof SubTask) {
                    removeNode(subTaskId);
                }
                // Рекурсивно удаляем каждую подзадачу
            }
        }
        if (nodeToRemove == null) return; // Если узел не найден, ничего не делаем

        // Обновляем ссылки для удаления узла
        if (nodeToRemove == head) {
            head = nodeToRemove.next;
            if (head != null) {
                head.prev = null;
            }
        } else if (nodeToRemove == tail) {
            tail = nodeToRemove.prev;
            if (tail != null) {
                tail.next = null;
            }
        } else {
            nodeToRemove.prev.next = nodeToRemove.next;
            nodeToRemove.next.prev = nodeToRemove.prev;
        }

        // Удаляем узел из HashMap
        taskMap.remove(taskId);

        // Обновляем размер списка
        size--;
    }


    // Метод для получения размера списка
    public int size() {
        return size;
    }

    // Метод для печати списка от головы до хвоста


    // Пример использования
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }
        return tasks;
    }
}



