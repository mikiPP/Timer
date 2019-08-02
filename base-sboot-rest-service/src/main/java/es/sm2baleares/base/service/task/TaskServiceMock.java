package es.sm2baleares.base.service.task;

import es.sm2baleares.base.model.api.task.TaskDto;
import es.sm2baleares.base.model.domain.Task;
import es.sm2baleares.base.model.domain.User;
import es.sm2baleares.base.service.exception.NotFoundException;
import es.sm2baleares.base.service.task.Converter.TaskConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service()
@ConditionalOnProperty(name = "test.active", havingValue = "true")
@Slf4j
public class TaskServiceMock implements TaskService {

    @Getter
    private List<Task> tasks;

    private User user;

    private TaskConverter taskConverter;

    public TaskServiceMock(TaskConverter taskConverter) {
        this.taskConverter = taskConverter;
    }

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

    @Override
    public List<TaskDto> findAll() {
        return taskConverter.toApiModel(tasks, TaskDto.class);
    }

    // Update

    @Override
    public TaskDto update(TaskDto taskDto) {
        Task task = findById(taskDto.getId());

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setActive(taskDto.getActive());

        tasks.add(task);

        return taskConverter.toApiModel(task, TaskDto.class);
    }

    // Delete

    @Override
    public void delete(Long id) {

        tasks.remove(findById(id));

    }

    @Override
    public void deleteAll() {

        tasks.clear();
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
}
