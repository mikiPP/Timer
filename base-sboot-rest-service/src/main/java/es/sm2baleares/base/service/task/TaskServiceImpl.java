package es.sm2baleares.base.service.task;

import es.sm2baleares.base.model.api.task.TaskDto;
import es.sm2baleares.base.model.domain.Task;
import es.sm2baleares.base.repository.TaskRepository;
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

    @Override
    public List<TaskDto> findAll() {

        return taskConverter.toApiModel(taskRepository.findAll(), TaskDto.class);

    }

    //Update

    @Override
    public TaskDto update(TaskDto taskDto) {

        Task task = taskRepository.findById(taskDto.getId()).get();

        if(!taskDto.getName().isEmpty() && taskDto.getName() != null) task.setName(taskDto.getName());
        if(!taskDto.getDescription().isEmpty() && taskDto.getDescription() != null) task.setDescription(taskDto.getDescription());
        if(taskDto.getActive() != null) task.setActive(taskDto.getActive());

        taskRepository.save(task);

        return taskConverter.toApiModel(task, TaskDto.class);
    }

    //Delete

    @Override
    public void delete(Long id) {
        taskRepository.delete(id);
    }

    @Override
    public void deleteAll() {
        taskRepository.deleteAll();
    }
}
