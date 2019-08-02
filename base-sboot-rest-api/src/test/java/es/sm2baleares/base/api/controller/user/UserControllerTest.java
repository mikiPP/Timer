package es.sm2baleares.base.api.controller.user;

import es.sm2baleares.base.IntegrationTest;
import es.sm2baleares.base.model.api.user.AuthUserDto;
import es.sm2baleares.base.model.api.user.UserDto;
import es.sm2baleares.base.model.domain.User;
import es.sm2baleares.base.service.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends IntegrationTest {

    private Long Id = 2l;


    @Override
    protected void initializeIntegrationTest() {

    }

    @Test
    public void createAnUserShouldReturn() throws Exception {

        /*-------------------------- Given  --------------------------*/

        String inputJson = super.mapToJson(createNewUser());

        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }


    @Test
    public void getAnUserByIdShouldReturnOneUser() throws Exception {

        /*-------------------------- Given  --------------------------*/

        mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(createNewUser()))).andReturn();

        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/users/id/" + (Id - 1))
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        UserDto userDto = super.mapFromJson(content, UserDto.class);

        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(userDto instanceof UserDto);
        assertTrue(userDto.getId().equals(Id - 1));


    }


    @Test
    public void findUserByUsernameShouldReturnBoolean() throws Exception {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = createNewUser();


        mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userDto))).andReturn();

        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/users/username/" + userDto.getUsername() + "fail")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Boolean userFindedTrue = super.mapFromJson(content, Boolean.class);


        MvcResult mvcResultFail =
                mvc.perform(MockMvcRequestBuilders.get("/api/users/username/" + userDto.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String contentFail = mvcResultFail.getResponse().getContentAsString();

        Boolean userFindedFalse = super.mapFromJson(contentFail, Boolean.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(userFindedTrue);
        assertFalse(userFindedFalse);


    }

    @Test(expected = NestedServletException.class)
    public void getUserByUsernameShouldReturnUser() throws Exception {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = createNewUser();


        mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userDto))).andReturn();

        /*-------------------------- When  --------------------------*/

        MvcResult mvcResultNull =
                mvc.perform(MockMvcRequestBuilders.get("/api/users/get/" + "fail")
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = mvcResultNull.getResponse().getContentAsString();

        UserDto  userNotFinded = super.mapFromJson(content, UserDto.class);


        MvcResult mvcResult =
                mvc.perform(MockMvcRequestBuilders.get("/api/users/get/" + userDto.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String contentFail = mvcResult.getResponse().getContentAsString();

        UserDto userFindedFalse = super.mapFromJson(contentFail, UserDto.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(HttpStatus.OK.value(), mvcResultNull.getResponse().getStatus());
        assertTrue(userNotFinded == null);
        assertFalse(userFindedFalse == null);


    }

    @Test
    public void findAllUsersShouldReturnListofUsers() throws Exception {

        /*-------------------------- Given  --------------------------*/

        mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(createNewUser()))).andReturn();

        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/users/all")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        List<UserDto> userDtos = super.mapFromJson(content, List.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(userDtos instanceof List);
        assertTrue(userDtos.size() > 0);

    }

    @Test
    public void UpdateAnUserShouldReturnUserUpdated() throws Exception {

        /*-------------------------- Given  --------------------------*/
        UserDto userDto = createNewUser();



        mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userDto))).andReturn();


        AuthUserDto userUpdated = new AuthUserDto();

        userUpdated.setOldUsername(userDto.getUsername());
        userUpdated.setOldPassword(userDto.getPassword());
        userUpdated.setNewPassword("antonio");
        userUpdated.setNewUsername("user updated");
        userUpdated.setActive(false);


        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/users/update")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userUpdated))).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        UserDto responseUserDto = super.mapFromJson(content, UserDto.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(responseUserDto instanceof UserDto);
        assertTrue(responseUserDto.getUsername().equals(userUpdated.getNewUsername()));

    }

    @Test
    public void DeleteAnUserByIdShouldReturnOk() throws Exception {

        /*-------------------------- Given  --------------------------*/




        mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(createNewUser()))).andReturn();


        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/users/id/" + (Id - 1))
                .contentType(MediaType.APPLICATION_JSON)).andReturn();




        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

    }


    @Test
    public void DeleteAnUserByUsernameShouldReturnOk() throws Exception {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = createNewUser();


        mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userDto))).andReturn();


        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/users/delete/"+userDto.getUsername()
        +"/" + userDto.getPassword())
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userDto))).andReturn();




        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

    }


    @Test
    public void DeleteAllAnUserShouldReturnOk() throws Exception {

        /*-------------------------- Given  --------------------------*/

        mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(createNewUser()))).andReturn();

        mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(createNewUser()))).andReturn();


        /*-------------------------- When  --------------------------*/

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/users/all")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        MvcResult mvcResultFindAll = mvc.perform(MockMvcRequestBuilders.get("/api/users/all")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = mvcResultFindAll.getResponse().getContentAsString();

        List<UserDto> userDtos = super.mapFromJson(content, List.class);


        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(userDtos.isEmpty());

    }


    @Test
    public void IfUserDontHasUsernameOrPasswordShouldReturnBadRequest() throws Exception {

        /*-------------------------- Given  --------------------------*/

        UserDto userDtoNull = new UserDto();

        UserDto userDtoWithoutPassword = new UserDto();
        userDtoWithoutPassword.setUsername("username Test");

        UserDto userDtoWithoutUsername = new UserDto();
        userDtoWithoutUsername.setPassword("Password Test");

        /*-------------------------- When  --------------------------*/

        MvcResult mvcAddRequestNull = mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userDtoNull))).andReturn();

        MvcResult mvcUpdateRequestNull = mvc.perform(MockMvcRequestBuilders.put("/api/users/update")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userDtoNull))).andReturn();


        MvcResult mvcAddRequest = mvc.perform(MockMvcRequestBuilders.post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userDtoWithoutPassword))).andReturn();

        MvcResult mvcUpdateRequest = mvc.perform(MockMvcRequestBuilders.put("/api/users/update")
                .contentType(MediaType.APPLICATION_JSON).content(super.mapToJson(userDtoWithoutUsername))).andReturn();



        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcAddRequest.getResponse().getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcUpdateRequest.getResponse().getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcAddRequestNull.getResponse().getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcUpdateRequestNull.getResponse().getStatus());


    }


    @Test(expected = NestedServletException.class)
    public void IfUserDontExistsShouldReturnNotFoundException() throws Exception {

        /*-------------------------- Given  --------------------------*/

        /*
         * Here i'm looking for cause NotFoundException, but that exception cause NestedServletException so i had
         * to put it
         * */


        /*-------------------------- When  --------------------------*/

        MvcResult mvcGetRequest = mvc.perform(MockMvcRequestBuilders.get("/api/users/id/" + 100)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        MvcResult mvcDeleteRequest = mvc.perform(MockMvcRequestBuilders.delete("/api/users/id/" + 100)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();



        /*-------------------------- Then  --------------------------*/

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcGetRequest.getResponse().getStatus());
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcDeleteRequest.getResponse().getStatus());

        Assert.fail("User with id: " + 100l + " don't exists");

    }


    private UserDto createNewUser() {

        UserDto userDto = new UserDto();

        userDto.setId(Id);
        userDto.setUsername("TestUsername" + Id);
        userDto.setPassword("12345678");
        userDto.setActive(true);

        Id++;
        return userDto;
    }
}
