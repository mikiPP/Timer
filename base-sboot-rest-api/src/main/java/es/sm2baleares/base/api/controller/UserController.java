package es.sm2baleares.base.api.controller;

import es.sm2baleares.base.api.ApiConstants;
import es.sm2baleares.base.model.api.user.AuthUserDto;
import es.sm2baleares.base.model.api.user.UserDto;
import es.sm2baleares.base.service.user.UserService;
import es.sm2baleares.spring.common.model.api.error.ApiError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
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
 * User REST controller.
 */
@Api(value = "Users endpoint", tags = "users")
@RestController
@RequestMapping(value = UserController.API_USERS_URL)
@Slf4j
public class UserController {

    static final String API_USERS_URL = ApiConstants.API_BASE_PATH + "/users";
    private static final String USER_ADD_URL = "/add";
    private static final String USER_ID_URL = "id/{id}";
    private static final String USER_ALL_URL = "/all";
    private static final String USER_UPDATE_URL = "/update";
    private static final String USER_USERNAME_URL = "username/{username}";
    private static final String USER_DELETE_URL = "/delete/{username}/{password}";
    private static final String USER_GET_BY_USERNAME_URL = "get/{username}";

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    // USER CRUD


    // Create

    @PostMapping(value = USER_ADD_URL)
    @ApiOperation(value = "create an user ")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "user inserted OK"),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 404, message = ApiConstants.NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public void insertUser(@ApiParam(value = "User value") @RequestBody @Valid UserDto userDto) {
        userService.insert(userDto);
    }

    //Read

    @GetMapping(value = USER_ID_URL)
    @ApiOperation(value = "get an user ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User returned", response = UserDto.class),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 401, message = ApiConstants.UNAUTHORIZED, response = ApiConstants.class),
            @ApiResponse(code = 403, message = ApiConstants.FORBIDDEN, response = ApiError.class),
            @ApiResponse(code = 404, message = ApiConstants.NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public UserDto findOneUserById(@ApiParam(value = "User's id") @PathVariable(value = "id") long id) {
        return userService.findOne(id).get();
    }


    @GetMapping(value = USER_GET_BY_USERNAME_URL)
    @ApiOperation(value = "get an user by username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User returned", response = UserDto.class),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 401, message = ApiConstants.UNAUTHORIZED, response = ApiConstants.class),
            @ApiResponse(code = 403, message = ApiConstants.FORBIDDEN, response = ApiError.class),
            @ApiResponse(code = 404, message = ApiConstants.NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public UserDto findUserByUsername(@ApiParam(value = "User's username") @PathVariable(value = "username") String username) {
        return userService.findOne(username).get();
    }


    @GetMapping(value = USER_ALL_URL)
    @ApiOperation(value = "get all users ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "all Users have been returned", response = List.class),
            @ApiResponse(code = 401, message = ApiConstants.UNAUTHORIZED, response = ApiConstants.class),
            @ApiResponse(code = 403, message = ApiConstants.FORBIDDEN, response = ApiError.class),
            @ApiResponse(code = 404, message = ApiConstants.NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public List<UserDto> findAllUsers() {
        return userService.findAll();
    }


    //Update

    @PutMapping(value = USER_UPDATE_URL)
    @ApiOperation(value = "update an user ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User has been updated"),
            @ApiResponse(code = 401, message = ApiConstants.UNAUTHORIZED, response = ApiConstants.class),
            @ApiResponse(code = 403, message = ApiConstants.FORBIDDEN, response = ApiError.class),
            @ApiResponse(code = 404, message = ApiConstants.NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public UserDto updateUser(@ApiParam(value = " updated User value") @RequestBody @Valid AuthUserDto authUserDto) {
        return userService.update(authUserDto);
    }

    //Delete

    @DeleteMapping(value = USER_ID_URL)
    @ApiOperation(value = "delete an user ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User has been deleted"),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 401, message = ApiConstants.UNAUTHORIZED, response = ApiConstants.class),
            @ApiResponse(code = 403, message = ApiConstants.FORBIDDEN, response = ApiError.class),
            @ApiResponse(code = 404, message = ApiConstants.NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public void deleteUserById(@ApiParam(value = "User's id") @PathVariable(value = "id") long id) {
        userService.delete(id);
    }

    @DeleteMapping(value = USER_DELETE_URL)
    @ApiOperation(value = "delete an user ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User has been deleted"),
            @ApiResponse(code = 401, message = ApiConstants.UNAUTHORIZED, response = ApiConstants.class),
            @ApiResponse(code = 403, message = ApiConstants.FORBIDDEN, response = ApiError.class),
            @ApiResponse(code = 404, message = ApiConstants.NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public void deleteUserByUsername(@ApiParam(value = "User Username") @PathVariable String username,
                                     @ApiParam(value = "User Password") @PathVariable String password) {
        userService.deleteUserByUsername(username, password);
    }


    @DeleteMapping(value = USER_ALL_URL)
    @ApiOperation(value = "delete all users ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "users have been deleted"),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiError.class),
            @ApiResponse(code = 401, message = ApiConstants.UNAUTHORIZED, response = ApiConstants.class),
            @ApiResponse(code = 403, message = ApiConstants.FORBIDDEN, response = ApiError.class),
            @ApiResponse(code = 404, message = ApiConstants.NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public void deleteAllUsers() {
        userService.deleteAll();
    }


    // Our calls

    /**
     * This request get one @param String Username
     * and @return Boolean
     * <p>
     * The logic is that check if the users already exists, if the user exists then return false,if not then return
     * true
     */

    @GetMapping(value = USER_USERNAME_URL)
    @ApiOperation(value = "get if user exists by username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User returned", response = Boolean.class),
            @ApiResponse(code = 400, message = ApiConstants.BAD_REQUEST_MESSAGE, response = ApiConstants.class),
            @ApiResponse(code = 404, message = ApiConstants.NOT_FOUND, response = ApiError.class),
            @ApiResponse(code = 500, message = ApiConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = ApiError.class)})
    public Boolean findIfUserExistsByUsername(@ApiParam(value = "User's username") @PathVariable(value = "username") String username) {
        return userService.usernameIsValid(username);
    }
}
