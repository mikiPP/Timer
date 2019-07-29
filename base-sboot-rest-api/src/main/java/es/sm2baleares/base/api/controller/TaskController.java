package es.sm2baleares.base.api.controller;

import es.sm2baleares.base.api.ApiConstants;
import es.sm2baleares.base.model.api.task.TaskDto;
import es.sm2baleares.base.service.task.TaskService;
import es.sm2baleares.spring.common.model.api.error.ApiError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Task REST controller.
 */
@Api(value = "Tasks endpoint", tags = "Tasks")
@RestController
@RequestMapping(value = TaskController.API_TASKS_URL)
@Slf4j
public class TaskController {

    static final String API_TASKS_URL = ApiConstants.API_BASE_PATH + "/tasks";
    private static final String TASK_ADD_URL = "/add";
    private static final String TASK_ID_URL = "/{id}";
    private static final String TASK_ALL_URL = "/all";
    private static final String TASK_UPDATE_URL = "/update";


    @Autowired
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    // TASK CRUD

    @PostMapping(value = TASK_ADD_URL)
    @ApiOperation(value = "create a task ")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "insert OK"),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public void insertTask(@ApiParam(value = "Task value") @RequestBody @Valid TaskDto taskDto) {
        taskService.insert(taskDto);
    }

    @GetMapping(value = TASK_ID_URL)
    @ApiOperation(value = "get a task ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "task returned", response = TaskDto.class),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public TaskDto findOneTask(@ApiParam(value = "task id") @PathVariable(value = "id") long id) {
        return taskService.findOne(id).get();
    }


    @GetMapping(value = TASK_ALL_URL)
    @ApiOperation(value = "get all tasks ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "all tasks have been returned", response = List.class),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public List<TaskDto> findAllTasks() {
        return taskService.findAll();
    }

    @PutMapping(value = TASK_UPDATE_URL)
    @ApiOperation(value = "update a task ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "task updated"),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public TaskDto updateTask(@ApiParam(value = " updated Task value") @RequestBody @Valid TaskDto taskDto) {
        return taskService.update(taskDto);
    }

    @DeleteMapping(value = TASK_ID_URL)
    @ApiOperation(value = "delete a task ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "task has been deleted"),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public void deleteTask(@ApiParam(value = "task id") @PathVariable(value = "id") long id) {
        taskService.delete(id);
    }


    @DeleteMapping(value = TASK_ALL_URL)
    @ApiOperation(value = "delete all tasks ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "tasks have been deleted"),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public void deleteAllTasks() {
        taskService.deleteAll();
    }
}