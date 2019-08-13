package es.sm2baleares.base.service.task;

import es.sm2baleares.base.IntegrationTest;
import es.sm2baleares.base.model.api.task.TaskDto;
import es.sm2baleares.base.model.api.task.TaskUpdateDto;
import es.sm2baleares.base.model.api.user.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
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
        taskDto.setUser(createUser());
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
    public void findOneByIdShouldReturnOneOptionalTaskDto() {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(6l);
        taskDto.setUser(createUser());
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
        assertEquals(task.get(), taskDto);
    }


    @Test
    public void findOneByNameShouldReturnOneOptionalTaskDto() {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(23l);
        taskDto.setUser(createUser());
        taskDto.setName("find by name test");
        taskDto.setDescription("find by name test DESCRIPTION");
        taskDto.setStart_time(LocalDateTime.now());
        taskDto.setEnd_time(taskDto.getStart_time().plusHours(2l));
        taskDto.setActive(true);

        taskService.insert(taskDto);


        /*-------------------------- When  --------------------------*/

        Optional<TaskDto> task = taskService.findOne("find by name test");

        /*-------------------------- Then  --------------------------*/

        assertTrue(task instanceof Optional);
        assertTrue(task.get() instanceof TaskDto);
        assertTrue(task.get().equals(taskDto));
    }


    @Test
    public void findOneByNameAndUserShouldReturnFalseIfExistsElseReturnTrue() {

        /*-------------------------- Given  --------------------------*/

        UserDto user = new UserDto();
        user.setId(4L);
        user.setUsername("username");
        user.setPassword("password");


        TaskDto taskDto = new TaskDto();

        taskDto.setId(535l);
        taskDto.setUser(user);
        taskDto.setName("find by name and user test");
        taskDto.setDescription("find by name and user test DESCRIPTION");
        taskDto.setStart_time(LocalDateTime.now());
        taskDto.setEnd_time(taskDto.getStart_time().plusHours(2l));
        taskDto.setActive(true);

        taskService.insert(taskDto);


        /*-------------------------- When  --------------------------*/

        Boolean isNotValid = taskService.checkIfTaskNameIsValid("find by name and user test", "username");
        Boolean isValid = taskService.checkIfTaskNameIsValid("find by name test", "random");

        /*-------------------------- Then  --------------------------*/

        assertTrue(isValid);
        assertFalse(isNotValid);

    }


    @Test
    public void findAllByUsernameShouldReturnListTask() {

        /*-------------------------- Given  --------------------------*/


        TaskDto taskDto = new TaskDto();

        taskDto.setId(535l);
        taskDto.setUser(createUser());
        taskDto.setName("find by name and user test");
        taskDto.setDescription("find by name and user test DESCRIPTION");
        taskDto.setStart_time(LocalDateTime.now());
        taskDto.setEnd_time(taskDto.getStart_time().plusHours(2l));
        taskDto.setActive(true);

        taskService.insert(taskDto);


        /*-------------------------- When  --------------------------*/

        List<TaskDto> tasksUser = taskService.findAllByUsername("username");

        /*-------------------------- Then  --------------------------*/

        assertTrue(tasksUser instanceof List);
        assertTrue(tasksUser.get(0).getUser().getUsername() == ("username"));

    }


    @Test
    public void findAllShouldReturnOneList() {

        /*-------------------------- Given  --------------------------*/

        TaskDto task = new TaskDto();

        task.setId(1l);
        task.setUser(createUser());
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
    public void UpdateTimeShouldReturnTheSameTaskUpdated() {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(73l);
        taskDto.setUser(createUser());
        taskDto.setName("task 73 name");
        taskDto.setDuration(2000);
        taskDto.setActive(true);

        taskService.insert(taskDto);


        TaskDto taskDtoUpdated = new TaskDto();
        taskDtoUpdated.setId(73l);
        taskDtoUpdated.setName("task 73 name");
        taskDtoUpdated.setStart_time(LocalDateTime.now());
        taskDtoUpdated.setEnd_time(taskDtoUpdated.getStart_time().plusHours(2l));
        taskDtoUpdated.setDuration(25000);
        taskDtoUpdated.setActive(false);


        /*-------------------------- When  --------------------------*/

        taskService.update(taskDtoUpdated);
        TaskDto taskDtoReturned = taskService.findOne("task 73 name").get();

        /*-------------------------- Then  --------------------------*/

        assertTrue(taskDtoReturned instanceof TaskDto);

        assertTrue(taskDtoReturned.getStart_time() != null
                && taskDtoUpdated.getStart_time().equals(taskDtoReturned.getStart_time()));

        assertTrue(taskDtoReturned.getEnd_time() != null
                && taskDtoUpdated.getEnd_time().equals(taskDtoReturned.getEnd_time()));

        assertTrue(taskDto.getActive() != taskDtoReturned.getActive()
                && taskDtoUpdated.getActive().equals(taskDtoReturned.getActive()));

        assertTrue(taskDto.getDuration() != taskDtoReturned.getDuration()
                && taskDtoUpdated.getDuration().equals(taskDtoReturned.getDuration()));

    }


    @Test
    public void UpdateNameAndDescriptionShouldReturnTheSameTaskUpdated() {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(37l);
        taskDto.setUser(createUser());
        taskDto.setName("task 37 name");
        taskDto.setDescription("task 37 description");
        taskDto.setActive(true);

        taskService.insert(taskDto);


        TaskUpdateDto taskDtoUpdated = new TaskUpdateDto();
        taskDtoUpdated.setOldName("task 37 name");
        taskDtoUpdated.setNewName("task 37 name updated");
        taskDtoUpdated.setNewDescription("task 37 description updated");


        /*-------------------------- When  --------------------------*/

        taskService.update(taskDtoUpdated);
        TaskDto taskDtoReturned = taskService.findOne(37l).get();

        /*-------------------------- Then  --------------------------*/

        assertTrue(taskDtoReturned instanceof TaskDto);

        assertTrue(taskDtoReturned.getName() != taskDtoUpdated.getOldName()
                && taskDtoUpdated.getNewName().equals(taskDtoReturned.getName()));

        assertTrue(taskDtoUpdated.getNewDescription().equals(taskDtoReturned.getDescription()));


    }


    @Test
    public void DeleteByIdShouldReturnVoid() {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(9l);
        taskDto.setUser(createUser());
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
    public void DeleteByNameShouldReturnVoid() {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(43l);
        taskDto.setUser(createUser());
        taskDto.setName("TASK43 NAME");
        taskDto.setDescription("TASK43 DESCRIPTION");
        taskDto.setStart_time(LocalDateTime.now());
        taskDto.setEnd_time(taskDto.getStart_time().plusHours(2l));
        taskDto.setActive(true);

        taskService.insert(taskDto);



        /*-------------------------- When  --------------------------*/

        int size = taskService.findAll().size();
        taskService.deleteByName("TASK43 NAME");

        /*-------------------------- Then  --------------------------*/

        assertTrue(size - 1 == taskService.findAll().size());


    }

    @Test
    public void DeleteAllShouldReturnVoid() {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = new TaskDto();

        taskDto.setId(12l);
        taskDto.setUser(createUser());
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


    public UserDto createUser() {
        UserDto user = new UserDto();
        user.setId(4L);
        user.setUsername("username");
        user.setPassword("password");
        user.setActive(true);
        return user;
    }

    @Override
    protected void initializeIntegrationTest() {

    }
}
