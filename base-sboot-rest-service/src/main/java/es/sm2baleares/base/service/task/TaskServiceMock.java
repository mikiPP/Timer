package es.sm2baleares.base.service.task;

import es.sm2baleares.base.model.api.task.TaskDto;
import es.sm2baleares.base.model.api.task.TaskUpdateDto;
import es.sm2baleares.base.model.domain.Task;
import es.sm2baleares.base.model.domain.User;
import es.sm2baleares.base.service.exception.NotFoundException;
import es.sm2baleares.base.service.task.Converter.TaskConverter;
import es.sm2baleares.base.service.user.converter.UserConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service()
@ConditionalOnProperty(name = "test.active", havingValue = "true")
@Slf4j
public class TaskServiceMock implements TaskService {

    @Getter
    private List<Task> tasks;

    private User user;

    @Autowired
    private TaskConverter taskConverter;

    @Autowired
    private UserConverter userConverter;


    @PostConstruct
    private void setUp() {

        tasks = new ArrayList<>();
        Task task = new Task(), task2 = new Task();
        user = new User();

        user.setId(1l);
        user.setUsername("Test");
        user.setPassword("abcdefg");
        user.setActive(true);

        task.setId(0l);
        task.setName("TASK1 NAME");
        task.setUser(user);
        task.setDescription("TASK1 DESCRIPTION");
        task.setStart_time(LocalDateTime.now());
        task.setEnd_time(task.getStart_time().plusHours(2l));
        task.setActive(true);

        task2.setId(1l);
        task2.setName("TASK2 NAME");
        task2.setUser(user);
        task2.setDescription("TASK2 DESCRIPTION");
        task2.setStart_time(LocalDateTime.now());
        task2.setEnd_time((task2.getStart_time().plusHours(2l)));
        task2.setActive(true);

        tasks.add(task);
        tasks.add(task2);

    }


    // CRUD


    // Create

    @Override
    public TaskDto insert(TaskDto taskDto) {
        Task task = taskConverter.toDomainModel(taskDto, Task.class);
        task.setActive(true);

        tasks.add(task);

        return taskConverter.toApiModel(task, TaskDto.class);
    }

    // Read

    @Override
    public Optional<TaskDto> findOne(Long id) {
        return Optional.of(taskConverter.toApiModel(findById(id), TaskDto.class));
    }

    public Optional<TaskDto> findOne(String name) {
        return Optional.of(taskConverter.toApiModel(findByName(name), TaskDto.class));
    }

    @Override
    public List<TaskDto> findAll() {
        return taskConverter.toApiModel(tasks, TaskDto.class);
    }

    // Update

    @Override
    public TaskDto update(TaskDto taskDto) {

        Task task = findByName(taskDto.getName());
        int index = findIndexByName(task.getName());


        if (taskDto.getActive() != null) task.setActive(taskDto.getActive());
        if (taskDto.getStart_time() != null) task.setStart_time(taskDto.getStart_time());
        if (taskDto.getEnd_time() != null) task.setEnd_time(taskDto.getEnd_time());
        if (taskDto.getDuration() != null) task.setDuration(taskDto.getDuration());
        if (taskDto.getTime_in() != null) task.setTime_in(taskDto.getTime_in());


        tasks.set(index, task);

        return taskConverter.toApiModel(task, TaskDto.class);
    }

    @Override
    public TaskDto update(TaskUpdateDto taskUpdateDto) {

        Task task = findByName(taskUpdateDto.getOldName());
        int index = findIndexByName(taskUpdateDto.getOldName());

        if (!taskUpdateDto.getNewName().isEmpty() && taskUpdateDto.getNewName() != null)
            task.setName(taskUpdateDto.getNewName());
        if (taskUpdateDto.getNewDescription() != null) task.setDescription(taskUpdateDto.getNewDescription());


        tasks.set(index, task);

        return taskConverter.toApiModel(task, TaskDto.class);
    }

    @Override
    public List<TaskDto> findAllByUsername(String username) {
        List<Task> userTasks = getTasks()
                .stream()
                .filter(task -> task.getUser().getUsername().equals(username))
                .collect(Collectors.toList());

        return taskConverter.toApiModel(userTasks, TaskDto.class);

    }

    // Delete

    @Override
    public void delete(Long id) {

        tasks.remove(findById(id));

    }

    @Override
    public void deleteByName(String name) {
        tasks.remove(findIndexByName(name));
    }


    @Override
    public void deleteAll() {

        tasks.clear();
    }


    // Others methods

    /**
     * @param name     String
     * @param username String
     * @return Boolean
     * <p>
     * <p>
     * This method checks if task exists for this user, if it exists return false because you cannot create another
     * task.In the other case, if doesn't exist then you can create this task
     */


    @Override
    public Boolean checkIfTaskNameIsValid(String name, String username) {

        for (Task task : tasks) {

            if (task.getName().equals(name) && task.getUser().getUsername().equals(username)) {
                return false;
            }
        }

        return true;
    }

    //Private Methods


    private Task findById(Long id) {

        Task taskToReturn = new Task();

        for (Task task : tasks) {

            if (task.getId().equals(id)) {
                taskToReturn = task;
                break;
            }
        }

        if (taskToReturn.getId() == null) {
            throw new NotFoundException("Task with id: " + id + " don't exists");
        }

        return taskToReturn;
    }


    private Task findByName(String name) {

        Task taskToReturn = new Task();

        for (Task task : tasks) {

            if (task.getName().equals(name)) {
                taskToReturn = task;
                break;
            }
        }

        if (taskToReturn.getId() == null) {
            throw new NotFoundException("Task with name: " + name + " don't exists");
        }

        return taskToReturn;
    }


    private int findIndexByName(String name) {

        int index = -1;

        for (Task task : tasks) {

            if (task.getName().equals(name)) {
                index = tasks.indexOf(task);
                break;
            }
        }

        if (index == -1) {
            throw new NotFoundException("Task with name: " + name + " don't exists");
        }

        return index;
    }
}
