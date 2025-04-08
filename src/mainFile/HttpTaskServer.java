package mainFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.*;
import tasks.*;
import taskmanager.TaskManager;
import manager.Managers;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Map;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        initializeHandlers();
    }

    class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.format(formatter));
            }
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return LocalDateTime.parse(in.nextString(), formatter);
        }
    }

    class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(JsonWriter out, Duration value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toMinutes()); // Сохраняем в минутах
            }
        }

        @Override
        public Duration read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return Duration.ofMinutes(in.nextLong()); // Читаем из минут
        }
    }

    private void initializeHandlers() {
        server.createContext("/tasks", new TasksHandler());
        server.createContext("/epics", new EpicsHandler());

        server.createContext("/subtasks", new SubtasksHandler());
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new PrioritizedHandler());

    }

    public void start() {
        server.start();
        System.out.println("HTTP сервер запущен на порту " + PORT);
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP сервер остановлен");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }

    abstract class BaseHandler implements HttpHandler {
        protected void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] resp = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, resp.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(resp);
            }
        }

        protected void sendSuccess(HttpExchange exchange, String response) throws IOException {
            sendResponse(exchange, 200, response);
        }

        protected void sendCreated(HttpExchange exchange, String response) throws IOException {
            sendResponse(exchange, 201, response);
        }

        protected void sendNotFound(HttpExchange exchange, String message) throws IOException {
            sendResponse(exchange, 404, gson.toJson(Map.of("error", "Not Found", "message", message)));
        }

        protected void sendNotAcceptable(HttpExchange exchange, String message) throws IOException {
            sendResponse(exchange, 406, gson.toJson(Map.of("error", "Not Acceptable", "message", message)));
        }

        protected void sendInternalError(HttpExchange exchange, String message) throws IOException {
            sendResponse(exchange, 500, gson.toJson(Map.of("error", "Internal Server Error", "message", message)));
        }

        protected String readRequestBody(HttpExchange exchange) throws IOException {
            return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    class TasksHandler extends BaseHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();

                if (method.equals("GET")) {
                    if (path.equals("/tasks")) {
                        String response = gson.toJson(taskManager.taskListGet());
                        sendSuccess(exchange, response);
                    } else if (path.matches("/tasks/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Task task = taskManager.getTask(id);
                        if (task != null) {
                            sendSuccess(exchange, gson.toJson(task));
                        } else {
                            sendNotFound(exchange, "Задача с ID " + id + " не найдена");
                        }
                    }
                } else if (method.equals("POST")) {
                    String requestBody = readRequestBody(exchange);
                    Task task = gson.fromJson(requestBody, Task.class);
                    try {
                        if (task.getId() == 0) {
                            taskManager.createTask(task);
                            sendCreated(exchange, gson.toJson(task));
                        } else {
                            taskManager.updateTasks(task);
                            sendSuccess(exchange, gson.toJson(task));
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        sendNotAcceptable(exchange, e.getMessage());

                    }
                } else if (method.equals("DELETE")) {
                    if (path.equals("/tasks")) {
                        taskManager.clearTasks();
                        sendSuccess(exchange, "Все задачи удалены");
                    } else if (path.matches("/tasks/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        taskManager.deleteTask(id);
                        sendSuccess(exchange, "Задача с ID " + id + " удалена");
                    }
                } else {
                    sendNotFound(exchange, "Метод не поддерживается");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendInternalError(exchange, e.getMessage());
            }
        }
    }

    // Аналогичные обработчики для SubtasksHandler, EpicsHandler, HistoryHandler, PrioritizedHandler
    class EpicsHandler extends BaseHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();

                if (method.equals("GET")) {
                    if (path.equals("/epics")) {
                        String respone = gson.toJson(taskManager.getEpicList());
                        sendSuccess(exchange, respone);
                    } else if (path.matches("/epics/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Epic epic = taskManager.getEpic(id);
                        if (epic != null) {
                            sendSuccess(exchange, gson.toJson(epic));
                        } else {
                            sendNotFound(exchange, "Задача с ID " + id + " не найдена");
                        }
                    }
                } else if (method.equals("POST")) {
                    String requestBody = readRequestBody(exchange);
                    Epic epic = gson.fromJson(requestBody, Epic.class);
                    try {
                        if (epic.getId() == 0) {
                            taskManager.createEpic(epic);
                            sendCreated(exchange, gson.toJson(epic));
                        } else {
                            taskManager.updatedEpic(epic);
                            sendSuccess(exchange, gson.toJson(epic));
                        }
                    } catch (IllegalArgumentException e) {
                        sendNotAcceptable(exchange, e.getMessage());
                    }
                } else if (method.equals("DELETE")) {
                    if (path.equals("/epics")) {
                        taskManager.clearEpics();
                        sendSuccess(exchange, "Все задачи удалены");
                    } else if (path.matches("/epics/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        taskManager.deleteEpic(id);
                        sendSuccess(exchange, "Задача с ID " + id + " удалена");
                    }
                } else {
                    sendNotFound(exchange, "Метод не поддерживается");
                }

            } catch (Exception e) {
                e.printStackTrace();
                sendInternalError(exchange, e.getMessage());
            }
        }


    }

    class SubtasksHandler extends BaseHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();

                if (method.equals("GET")) {
                    if (path.equals("/subtasks")) {
                        String respone = gson.toJson(taskManager.subTaskListGet());
                        sendSuccess(exchange, respone);
                    } else if (path.matches("/subtasks/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        SubTask epic = taskManager.getSubTask(id);
                        if (epic != null) {
                            sendSuccess(exchange, gson.toJson(epic));
                        } else {
                            sendNotFound(exchange, "Задача с ID " + id + " не найдена");
                        }
                    }
                } else if (method.equals("POST")) {
                    String requestBody = readRequestBody(exchange);
                    SubTask subTask = gson.fromJson(requestBody, SubTask.class);
                    try {
                        if (subTask.getId() == 0) {
                            taskManager.createSubTask(subTask);
                            sendCreated(exchange, gson.toJson(subTask));
                        } else {
                            taskManager.updateSubTasks(subTask);
                            sendSuccess(exchange, gson.toJson(subTask));
                        }
                    } catch (IllegalArgumentException e) {
                        sendNotAcceptable(exchange, e.getMessage());
                    }
                } else if (method.equals("DELETE")) {
                    if (path.equals("/subtasks")) {
                        taskManager.clearSubTasks();
                        sendSuccess(exchange, "Все задачи удалены");
                    } else if (path.matches("/epics/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        taskManager.deleteSubTask(id);
                        sendSuccess(exchange, "Задача с ID " + id + " удалена");
                    }
                } else {
                    sendNotFound(exchange, "Метод не поддерживается");
                }

            } catch (Exception e) {
                e.printStackTrace();
                sendInternalError(exchange, e.getMessage());
            }
        }
    }

    class HistoryHandler extends BaseHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                if (exchange.getRequestMethod().equals("GET")) {
                    String response = gson.toJson(taskManager.getHistory());
                    sendSuccess(exchange, response);
                } else {
                    sendNotFound(exchange, "Метод не поддерживается");
                }
            } catch (Exception e) {
                sendInternalError(exchange, e.getMessage());
            }
        }
    }

    class PrioritizedHandler extends BaseHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                if (exchange.getRequestMethod().equals("GET")) {
                    String response = gson.toJson(taskManager.getPrioritized());
                    sendSuccess(exchange, response);
                } else {
                    sendNotFound(exchange, "Метод не поддерживается");
                }
            } catch (Exception e) {
                sendInternalError(exchange, e.getMessage());
            }
        }
    }
}