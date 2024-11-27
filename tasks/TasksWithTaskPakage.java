
import Tasks.*;

import Manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TasksWithTaskPakage{

    @Test
    public void TestTaskEqualityById(){
        Task task1=new Task();
        Task task2=new Task();
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1,task2);
    }
    @Test
    public void TestSubclassEqualityById(){
        Task task1=new Epic();
        Task task2=new Epic();
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1,task2);
        SubTask task3=new SubTask();
        SubTask task4=new SubTask();
        task3.setId(1);
        task4.setId(1);
        assertEquals(task3,task4);
    }




}
