package es.sm2baleares.base.service.task;

import es.sm2baleares.base.IntegrationTest;
import es.sm2baleares.base.model.api.task.TaskDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;


@RunWith(SpringRunner.class)
public class TaskServiceTest extends IntegrationTest {


    @Autowired
    private TaskService taskService;


    @Test
    public void InsertOneTaskShouldReturnTheTask() {


        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(4l);
        taskDto.setName("TASK4 NAME");
        taskDto.setDescription("TASK4 DESCRIPTION");
        taskDto.setStart_time(LocalDateTime.now());
        taskDto.setEnd_time(taskDto.getStart_time().plusHours(2l));
        taskDto.setActive(true);



        /*-------------------------- When  --------------------------*/

        int numberOfTasks = taskService.findAll().size();
        TaskDto taskDtoTest = taskService.insert(taskDto);


        /*-------------------------- Then  --------------------------*/

        assertTrue(numberOfTasks + 1 == taskService.findAll().size());
        assertTrue(taskDto.equals(taskDtoTest));

    }


    @Test
    public void findOneShouldReturnOneOptionalTaskDto() {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(6l);
        taskDto.setName("TASK6 NAME");
        taskDto.setDescription("TASK6 DESCRIPTION");
        taskDto.setStart_time(LocalDateTime.now());
        taskDto.setEnd_time(taskDto.getStart_time().plusHours(2l));
        taskDto.setActive(true);

        taskService.insert(taskDto);


        /*-------------------------- When  --------------------------*/

        Optional<TaskDto> task = taskService.findOne(6l);

        /*-------------------------- Then  --------------------------*/

        assertTrue(task instanceof Optional);
        assertTrue(task.get() instanceof TaskDto);
        assertTrue(task.get().equals(taskDto));
    }


    @Test
    public void findAllShouldReturnOneList() {

        /*-------------------------- Given  --------------------------*/

        TaskDto task = new TaskDto();

        task.setId(1l);
        task.setName("TASK1 NAME");
        task.setDescription("TASK1 DESCRIPTION");
        task.setStart_time(LocalDateTime.now());
        task.setEnd_time(task.getStart_time().plusHours(2l));
        task.setActive(true);


        /*-------------------------- When  --------------------------*/

        List<TaskDto> tasks = taskService.findAll();
        tasks.add(task);

        /*-------------------------- Then  --------------------------*/

        assertTrue(tasks instanceof List);
        assertTrue(tasks.get(0) instanceof TaskDto);

    }


    @Test
    public void UpdateShouldReturnTheSameTaskUpdated() {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(8l);
        taskDto.setName("TASK8 NAME");
        taskDto.setDescription("TASK8 DESCRIPTION");
        taskDto.setStart_time(LocalDateTime.now());
        taskDto.setEnd_time(taskDto.getStart_time().plusHours(2l));
        taskDto.setActive(true);

        taskService.insert(taskDto);


        TaskDto taskDtoUpdated = new TaskDto();
        taskDtoUpdated.setId(8l);
        taskDtoUpdated.setName("TASK8 NAME Updated");
        taskDtoUpdated.setDescription("TASK8 DESCRIPTION Updated");
        taskDtoUpdated.setActive(false);


        /*-------------------------- When  --------------------------*/

        taskService.update(taskDtoUpdated);
        TaskDto taskDtoReturned = taskService.findOne(8l).get();

        /*-------------------------- Then  --------------------------*/

        assertTrue(taskDtoReturned instanceof TaskDto);

        assertTrue(taskDto.getName() != taskDtoReturned.getName()
                && taskDtoUpdated.getName().equals(taskDtoReturned.getName()));

        assertTrue(taskDto.getDescription() != taskDtoReturned.getDescription()
                && taskDtoUpdated.getDescription().equals(taskDtoReturned.getDescription()));

        assertTrue(taskDto.getActive() != taskDtoReturned.getActive()
                && taskDtoUpdated.getActive().equals(taskDtoReturned.getActive()));

    }


    @Test
    public void DeleteShouldReturnVoid() {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(9l);
        taskDto.setName("TASK9 NAME");
        taskDto.setDescription("TASK9 DESCRIPTION");
        taskDto.setStart_time(LocalDateTime.now());
        taskDto.setEnd_time(taskDto.getStart_time().plusHours(2l));
        taskDto.setActive(true);

        taskService.insert(taskDto);



        /*-------------------------- When  --------------------------*/

        int size = taskService.findAll().size();
        taskService.delete(9l);

        /*-------------------------- Then  --------------------------*/

        assertTrue(size - 1 == taskService.findAll().size());


    }

    @Test
    public void DeleteAllShouldReturnVoid() {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(12l);
        taskDto.setName("TASK12 NAME");
        taskDto.setDescription("TASK12 DESCRIPTION");
        taskDto.setStart_time(LocalDateTime.now());
        taskDto.setEnd_time(taskDto.getStart_time().plusHours(2l));
        taskDto.setActive(true);

        taskService.insert(taskDto);


        TaskDto taskDto2 = new TaskDto();
        taskDto.setId(13l);
        taskDto.setName("TASK13 NAME Updated");
        taskDto.setDescription("TASK13 DESCRIPTION Updated");
        taskDto.setActive(false);

        taskService.insert(taskDto2);

        /*-------------------------- When  --------------------------*/
        int tasksCount = taskService.findAll().size();
        taskService.deleteAll();

        /*-------------------------- Then  --------------------------*/

        assertTrue(taskService.findAll().size() == 0);
        assertTrue(tasksCount > 0);

    }

    @Override
    protected void initializeIntegrationTest() {

    }
}
