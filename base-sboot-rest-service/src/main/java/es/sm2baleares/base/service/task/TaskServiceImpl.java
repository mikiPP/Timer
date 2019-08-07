package es.sm2baleares.base.service.task;

import es.sm2baleares.base.model.api.task.TaskDto;
import es.sm2baleares.base.model.api.task.TaskUpdateDto;
import es.sm2baleares.base.model.domain.Task;
import es.sm2baleares.base.model.domain.User;
import es.sm2baleares.base.repository.TaskRepository;
import es.sm2baleares.base.repository.UserRepository;
import es.sm2baleares.base.service.task.Converter.TaskConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service()
@ConditionalOnProperty(name = "test.active", havingValue = "false")
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskConverter taskConverter;

    @Autowired
    private UserRepository userRepository;


    // CRUD


    //Create

    @Override
    public TaskDto insert(TaskDto taskDto) {

        Task task = taskConverter.toDomainModel(taskDto, Task.class);
        task.setActive(true);

        taskRepository.save(task);

        return taskConverter.toApiModel(task, TaskDto.class);

    }

    //Read


    @Override
    public Optional<TaskDto> findOne(Long id) {

        return Optional.of(taskConverter.toApiModel(taskRepository.findById(id).get(), TaskDto.class));

    }


    public Optional<TaskDto> findOne(String name) {

        return Optional.of(taskConverter.toApiModel(taskRepository.findByName(name).get(), TaskDto.class));

    }

    @Override
    public List<TaskDto> findAll() {

        return taskConverter.toApiModel(taskRepository.findAll(), TaskDto.class);

    }


    @Override
    public List<TaskDto> findAllByUsername(String username) {

        User user = userRepository.findByUsername(username).get();

        return taskConverter.toApiModel(taskRepository.findAllTasksById(user.getId()), TaskDto.class);
    }

    //Update

    @Override
    public TaskDto update(TaskDto taskDto) {

        Task task = taskRepository.findByName(taskDto.getName()).get();


        if (taskDto.getActive() != null) task.setActive(taskDto.getActive());
        if (taskDto.getStart_time() != null) task.setStart_time(taskDto.getStart_time());
        if (taskDto.getEnd_time() != null) task.setEnd_time(taskDto.getEnd_time());
        if (taskDto.getDuration() != null) task.setDuration(taskDto.getDuration());


        taskRepository.save(task);

        return taskConverter.toApiModel(task, TaskDto.class);
    }


    @Override
    public TaskDto update(TaskUpdateDto taskUpdateDto) {

        Task task = taskRepository.findByName(taskUpdateDto.getOldName()).get();

        if (!taskUpdateDto.getNewName().isEmpty() && taskUpdateDto.getNewName() != null)
            task.setName(taskUpdateDto.getNewName());
        if (taskUpdateDto.getNewDescription() != null) task.setDescription(taskUpdateDto.getNewDescription());
        //añadir duración

        taskRepository.save(task);

        return taskConverter.toApiModel(task, TaskDto.class);
    }


    //Delete

    @Override
    public void delete(Long id) {
        taskRepository.delete(id);
    }

    @Override
    public void deleteByName(String name) {
        taskRepository.delete(taskConverter.toDomainModel(findOne(name).get(), Task.class));
    }

    @Override
    public void deleteAll() {
        taskRepository.deleteAll();
    }

    /* Others methods*/


    /**
     * @param name     String
     * @param username user
     * @return Boolean
     * <p>
     * <p>
     *  This method checks if task exists for this user, if it exists return false because you cannot create another
     *      * task.In the other case, if doesn't exist then you can create this task
     */


    @Override
    public Boolean checkIfTaskNameIsValid(String name, String username) {


        return (taskRepository.finTasksByIdAndName(userRepository.findByUsername(username).get().getId(), name) != null)
                ? true : false;
    }

}
