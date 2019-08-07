package es.sm2baleares.base.service.task;

import es.sm2baleares.base.model.api.task.TaskDto;
import es.sm2baleares.base.model.api.task.TaskUpdateDto;
import es.sm2baleares.spring.common.service.common.BaseService;

import java.util.List;
import java.util.Optional;

public interface TaskService extends BaseService<Long, TaskDto> {


    void deleteByName(String name);

    Boolean checkIfTaskNameIsValid(String name, String username);

    Optional<TaskDto> findOne(String name);

    TaskDto update(TaskUpdateDto taskUpdateDto);

    List<TaskDto> findAllByUsername(String username);
}
