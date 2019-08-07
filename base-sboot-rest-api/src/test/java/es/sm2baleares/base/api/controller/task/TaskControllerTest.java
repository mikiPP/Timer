package es.sm2baleares.base.api.controller.task;

import es.sm2baleares.base.IntegrationTest;
import es.sm2baleares.base.model.api.task.TaskDto;
import es.sm2baleares.base.model.api.task.TaskUpdateDto;
import es.sm2baleares.base.model.api.user.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class TaskControllerTest extends IntegrationTest {

    private Long Id = 2l;


    @Override
    protected void initializeIntegrationTest() {

    }

    @Test
    public void createATaskShouldReturnTheSameTask() throws Exception {

        /*-------------------------- Given  --------------------------*/

        String inputJson = super.mapToJson(createNewTask());

        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }


    @Test
    public void findATaskByIdShouldReturnTheTask() throws Exception {

        /*-------------------------- Given  --------------------------*/

        mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(createNewTask()))).andReturn();

        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/tasks/id/" + (Id - 1))
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        TaskDto taskDto = super.mapFromJson(content, TaskDto.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(taskDto instanceof TaskDto);
        assertTrue(taskDto.getId() == Id - 1);


    }


    @Test
    public void findTaskByUserUsernameShouldReturnList() throws Exception {

        /*-------------------------- Given  --------------------------*/

        mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(createNewTask()))).andReturn();

        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/tasks/all/username")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        List<TaskDto> taskDtos = super.mapFromJson(content, List.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(taskDtos instanceof List);
        assertTrue(taskDtos.size() > 0);

    }


    @Test
    public void findTaskByUsernameAndNameShouldReturnBoolean() throws Exception {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = createNewTask();


        mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(taskDto))).andReturn();

        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(
                "/api/tasks/name/" + taskDto.getName() + "/user/" + taskDto.getUser().getUsername())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Boolean isUsed = super.mapFromJson(content, Boolean.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(isUsed);

    }

    @Test
    public void findAllTasksShouldReturnListOfTaskDtos() throws Exception {

        /*-------------------------- Given  --------------------------*/

        mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(createNewTask()))).andReturn();


        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/tasks/all")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        List<TaskDto> taskDtos = super.mapFromJson(content, List.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(taskDtos instanceof List);
        assertTrue(taskDtos.size() > 0);

    }

    @Test
    public void UpdateATaskNameShouldReturnTaskUpdated() throws Exception {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = createNewTask();

        mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(taskDto))).andReturn();


        TaskUpdateDto taskDtoUpdated = new TaskUpdateDto();

        taskDtoUpdated.setOldName(taskDto.getName());
        taskDtoUpdated.setNewName("antonio");
        taskDtoUpdated.setNewDescription("description");


        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/tasks/update")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(taskDtoUpdated))).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        TaskDto taskDtoUpdatedReturned = super.mapFromJson(content, TaskDto.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(taskDtoUpdatedReturned instanceof TaskDto);
        assertFalse(taskDto.equals(taskDtoUpdated));
        assertTrue(taskDtoUpdated.getNewName().equals(taskDtoUpdatedReturned.getName()) &&
                taskDtoUpdated.getNewDescription().equals(taskDtoUpdatedReturned.getDescription()));
    }


    @Test
    public void UpdateATaskTimeShouldReturnTaskUpdated() throws Exception {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = createNewTask();


        mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(taskDto))).andReturn();


        UserDto userDto = new UserDto();
        userDto.setPassword("password");
        userDto.setUsername("username");
        userDto.setId(4l);


        TaskDto taskDtoUpdated = new TaskDto();
        taskDtoUpdated.setId(taskDto.getId());
        taskDtoUpdated.setName(taskDto.getName());
        taskDtoUpdated.setUser(userDto);
        taskDtoUpdated.setStart_time(LocalDateTime.now());
        taskDtoUpdated.setEnd_time(LocalDateTime.now());
        taskDtoUpdated.setDescription(taskDto.getDescription());
        taskDtoUpdated.setDuration(2000);
        taskDtoUpdated.setActive(true);


        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/tasks/update/time")
                .contentType(MediaType.APPLICATION_JSON).content("{\"id\":2,\"name\":\"TestName2\"," +
                        "\"user\":{\"id\":4,\"username\":\"username\",\"password\":\"password\",\"active\":null}," +
                        "\"description\":\"Description 2\",\"start_time\":\"2019-08-07T10:37:39.262\"," +
                        "\"end_time\":\"2019-08-07T12:37:39\",\"duration\":2000,\"active\":true}\"")).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        TaskDto taskDtoUpdatedReturned = super.mapFromJson(content, TaskDto.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(taskDtoUpdatedReturned instanceof TaskDto);
        assertTrue(taskDtoUpdatedReturned.getStart_time() != null &&
                taskDtoUpdatedReturned.getEnd_time() != null);

    }


    @Test
    public void DeleteATaskByIdShouldReturnOk() throws Exception {

        /*-------------------------- Given  --------------------------*/

        mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(createNewTask()))).andReturn();


        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/tasks/id/" + (Id - 1))
                .contentType(MediaType.APPLICATION_JSON)).andReturn();



        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

    }


    @Test
    public void DeleteATaskByNameShouldReturnOk() throws Exception {

        /*-------------------------- Given  --------------------------*/


        TaskDto taskDto = createNewTask();

        mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(taskDto))).andReturn();


        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/tasks/name/" + taskDto.getName())
                .contentType(MediaType.APPLICATION_JSON)).andReturn();



        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

    }

    @Test
    public void DeleteAllATaskShouldReturnOk() throws Exception {

        /*-------------------------- Given  --------------------------*/

        mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(createNewTask()))).andReturn();

        mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(createNewTask()))).andReturn();


        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/tasks/all")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        MvcResult mvcResultFindAll = mvc.perform(MockMvcRequestBuilders.get("/api/tasks/all")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = mvcResultFindAll.getResponse().getContentAsString();

        List<TaskDto> taskDtos = super.mapFromJson(content, List.class);



        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(taskDtos.isEmpty());

    }

    @Test
    public void IfTaskDontHasNameShouldReturnBadRequest() throws Exception {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = new UserDto();
        userDto.setPassword("testpasssword");



        /*-------------------------- When  --------------------------*/

        MvcResult mvcAddRequest = mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userDto))).andReturn();

        MvcResult mvcUpdateRequest = mvc.perform(MockMvcRequestBuilders.put("/api/tasks/update")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userDto))).andReturn();



        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcAddRequest.getResponse().getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcUpdateRequest.getResponse().getStatus());

    }


    @Test(expected = NestedServletException.class)
    public void IfTaskDontExistsShouldReturnNotFoundException() throws Exception {

        /*-------------------------- Given  --------------------------*/

        /*
         * Here i'm looking for cause NotFoundException, but that exception cause NestedServletException so i had
         * to put it
         * */


        /*-------------------------- When  --------------------------*/

        MvcResult mvcGetRequest = mvc.perform(MockMvcRequestBuilders.get("/api/tasks/id/" + 100)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        MvcResult mvcDeleteRequest = mvc.perform(MockMvcRequestBuilders.delete("/api/tasks/id/" + 100)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();



        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcGetRequest.getResponse().getStatus());
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcDeleteRequest.getResponse().getStatus());


    }


    private TaskDto createNewTask() {

        UserDto userDto = new UserDto();
        userDto.setId(2l);
        userDto.setUsername("username");
        userDto.setPassword("password");

        TaskDto taskDto = new TaskDto();
        taskDto.setId(Id);
        taskDto.setUser(userDto);
        taskDto.setName("TestName" + Id);
        taskDto.setDescription("Description " + Id);
        taskDto.setActive(true);

        Id++;
        return taskDto;
    }
}
