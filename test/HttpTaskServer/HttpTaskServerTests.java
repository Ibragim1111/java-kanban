package HttpTaskServer;
import being.taskstypes.TaskType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import com.example.status.Status;
import taskmanager.InMemoryTaskManager;
import taskmanager.TaskManager;
import mainFile.HttpTaskServer;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private static HttpTaskServer server;
    private static TaskManager manager;
    private static HttpClient client;
    private static Gson gson;

    @BeforeEach
    void beforeAll() throws IOException {
        manager = new InMemoryTaskManager();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        client = HttpClient.newHttpClient();
    }

    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
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

    static class DurationAdapter extends TypeAdapter<Duration> {
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

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpTaskServer(manager);
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void testCreateTask() throws IOException, InterruptedException {
        Task task = new Task (0,TaskType.TASK, "Test Task","Test Description", Status.NEW);
        task.setDuration(Duration.ofMinutes(1));
        task.setStartTime(LocalDateTime.of(2022,1,12,1,2,9));

        String taskJson = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Task createdTask = gson.fromJson(response.body(), Task.class);
        assertNotNull(createdTask.getId());
        assertEquals("Test Task", createdTask.getName());
    }

    @Test
    void testGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task (1,TaskType.TASK, "Test Task","Test Description", Status.NEW);
        task.setDuration(Duration.ofMinutes(1));
        task.setStartTime(LocalDateTime.of(2022,1,12,1,2,9));
        manager.createTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getName());
    }
    @Test
    void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task (1,TaskType.TASK, "Test Task","Test Description", Status.NEW);
        task.setDuration(Duration.ofMinutes(1));
        task.setStartTime(LocalDateTime.of(2022,1,12,1,2,9));

        manager.createTask(task);
        int taskId = task.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + taskId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task retrievedTask = gson.fromJson(response.body(), Task.class);
        assertEquals(taskId, retrievedTask.getId());
        assertEquals("Test Task", retrievedTask.getName());
    }

    @Test
    void testCreateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic (0,TaskType.TASK, "Test Epic","Test Description", Status.NEW);
        epic.setDuration(Duration.ofMinutes(1));
        epic.setStartTime(LocalDateTime.of(2022,1,12,1,2,9));


        String epicJson = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Epic createdEpic = gson.fromJson(response.body(), Epic.class);
        assertNotEquals(0, createdEpic.getId());
        assertEquals("Test Epic", createdEpic.getName());
    }

    @Test
    void testCreateSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic (1,TaskType.TASK, "Test Epic","Test Description", Status.NEW);
        epic.setDuration(Duration.ofMinutes(1));
        epic.setStartTime(LocalDateTime.of(2022,1,12,1,2,9));
        manager.createEpic(epic);
        int epicId = epic.getId();

        SubTask subTask = new SubTask(0,TaskType.TASK, "Test SubTask","Test Description", Status.NEW ,epicId);
        subTask.setDuration(Duration.ofMinutes(1));
        subTask.setStartTime(LocalDateTime.of(2022, 1, 12, 1, 2, 9));

        String subTaskJson = gson.toJson(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        SubTask createdSubTask = gson.fromJson(response.body(), SubTask.class);
        assertNotEquals(0, createdSubTask.getId());
        assertEquals("Test SubTask", createdSubTask.getName());
        assertEquals(epicId, createdSubTask.getEpicID());
    }

    @Test
    void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task(1,TaskType.TASK, "Test task","Test Description", Status.NEW);
        manager.createTask(task);
        manager.getTask(task.getId()); // Добавляем в историю

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> history = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertFalse(history.isEmpty());
        assertEquals(task.getId(), history.get(0).getId());
    }

    @Test
    void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task = new Task(1,TaskType.TASK, "Test Task","Test Description", Status.NEW);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(30));
        manager.createTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> prioritized = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertFalse(prioritized.isEmpty());
        assertEquals(task.getId(), prioritized.get(0).getId());
    }

}