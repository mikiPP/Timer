package es.sm2baleares.base.api.controller.task;

import es.sm2baleares.base.IntegrationTest;
import es.sm2baleares.base.model.api.task.TaskDto;
import es.sm2baleares.base.model.api.user.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

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
    public void createATaskShouldReturn() throws Exception {

        /*-------------------------- Given  --------------------------*/

        String inputJson = super.mapToJson(createNewTask());

        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }


    @Test
    public void getATaskShouldReturnATask() throws Exception {

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
    public void UpdateATaskShouldReturnTaskUpdated() throws Exception {

        /*-------------------------- Given  --------------------------*/

        TaskDto taskDto = createNewTask();

        mvc.perform(MockMvcRequestBuilders.post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(taskDto))).andReturn();


        TaskDto taskDtoUpdated = new TaskDto();

        taskDtoUpdated.setId(Id - 1);
        taskDtoUpdated.setName("antonio");
        taskDtoUpdated.setActive(false);


        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/tasks/update")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(taskDtoUpdated))).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        TaskDto taskDtoUpdatedReturned = super.mapFromJson(content, TaskDto.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(taskDtoUpdatedReturned instanceof TaskDto);
        assertFalse(taskDto.equals(taskDtoUpdated));
        assertTrue(taskDtoUpdated.equals(taskDtoUpdatedReturned));

    }

    @Test
    public void DeleteATaskShouldReturnOk() throws Exception {

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

        TaskDto taskDto = new TaskDto();

        taskDto.setId(Id);
        taskDto.setName("TestName" + Id);
        taskDto.setActive(true);

        Id++;
        return taskDto;
    }
}
