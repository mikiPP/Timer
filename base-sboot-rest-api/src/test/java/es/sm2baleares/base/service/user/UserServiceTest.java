package es.sm2baleares.base.service.user;

import es.sm2baleares.base.IntegrationTest;
import es.sm2baleares.base.model.api.user.AuthUserDto;
import es.sm2baleares.base.model.api.user.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


@RunWith(SpringRunner.class)
public class UserServiceTest extends IntegrationTest {

    private Long Id = 3l;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Test
    public void InsertOneUserShouldReturnTheUser() {


        /*-------------------------- Given  --------------------------*/

        UserDto userDto = createUser();


        /*-------------------------- When  --------------------------*/

        int users = userService.findAll().size();

        UserDto userInserted = userService.insert(userDto);

        /*-------------------------- Then  --------------------------*/

        assertTrue(userInserted.getActive());

        assertTrue(userInserted.getUsername().equals(userDto.getUsername()));

        assertTrue(userDto.getPassword() != userInserted.getPassword());

        assertTrue(bCryptPasswordEncoder.matches("12345678", userInserted.getPassword()));

        assertTrue(users + 1 == userService.findAll().size());
    }


    @Test
    public void findOneByIdShouldReturnOneOptionalUserDto() {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = createUser();
        userDto.setId(20000l);

        userService.insert(userDto);

        /*-------------------------- When  --------------------------*/

        Optional<UserDto> userFinded = userService.findOne(userDto.getId());

        /*-------------------------- Then  --------------------------*/

        assertTrue(userFinded instanceof Optional);

        assertTrue(userFinded.get() instanceof UserDto);

        assertEquals(userDto.getId(), userFinded.get().getId());
        assertEquals(userDto.getUsername(), userFinded.get().getUsername());
        assertEquals(userDto.getActive(), userFinded.get().getActive());

        assertTrue(bCryptPasswordEncoder.matches("12345678", userFinded.get().getPassword()));

    }


    @Test
    public void findOneByUsernameShouldReturnOneOptionalUserDto() {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = createUser();

        userService.insert(userDto);

        /*-------------------------- When  --------------------------*/

        Optional<UserDto> userFinded = userService.findOne(userDto.getUsername());

        /*-------------------------- Then  --------------------------*/

        assertTrue(userFinded instanceof Optional);

        assertTrue(userFinded.get() instanceof UserDto);

        assertEquals(userDto.getId(), userFinded.get().getId());
        assertEquals(userDto.getUsername(), userFinded.get().getUsername());
        assertEquals(userDto.getActive(), userFinded.get().getActive());

        assertTrue(bCryptPasswordEncoder.matches("12345678", userFinded.get().getPassword()));

    }

    @Test
    public void findAllShouldReturnOneList() {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = createUser();

        userService.insert(userDto);

        UserDto userDto2 = createUser();

        userService.insert(userDto2);

        /*-------------------------- When  --------------------------*/

        int usersCount = userService.findAll().size();


        /*-------------------------- Then  --------------------------*/

        assertTrue(userService.findAll() instanceof List);
        assertTrue(usersCount > 0);

    }


    @Test
    public void UpdateShouldReturnTheSameUserUpdated() {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = createUser();

        userService.insert(userDto);

        AuthUserDto authUserDtoToUpdate = new AuthUserDto();
        authUserDtoToUpdate.setOldUsername(userDto.getUsername());
        authUserDtoToUpdate.setOldPassword(userDto.getPassword());
        authUserDtoToUpdate.setNewUsername("username7 updated");
        authUserDtoToUpdate.setNewPassword(Base64.getMimeEncoder().encodeToString("12345678 Update".getBytes()));
        authUserDtoToUpdate.setActive(false);

        /*-------------------------- When  --------------------------*/

        userService.update(authUserDtoToUpdate);

        UserDto userUpdated = userService.findOne(authUserDtoToUpdate.getNewUsername()).get();


        /*-------------------------- Then  --------------------------*/

        assertTrue(userUpdated instanceof UserDto);


        assertTrue(userDto.getUsername() != userUpdated.getUsername()
                && userUpdated.getUsername().equals(authUserDtoToUpdate.getNewUsername()));

        assertTrue(!bCryptPasswordEncoder.matches(userDto.getPassword(), userUpdated.getPassword())
                && bCryptPasswordEncoder.matches(authUserDtoToUpdate.getNewPassword(), userUpdated.getPassword()));


        assertTrue(userDto.getActive() != userUpdated.getActive()
                && authUserDtoToUpdate.getActive() == userUpdated.getActive());


    }


    @Test
    public void DeleteUserByIdShouldReturnVoid() {

        /*-------------------------- Given  --------------------------*/
        UserDto userDto = createUser();

        userService.insert(userDto);

        /*-------------------------- When  --------------------------*/

        int usersCount = userService.findAll().size();
        userService.delete(userDto.getId());

        /*-------------------------- Then  --------------------------*/

        assertTrue(usersCount - 1 == userService.findAll().size());


    }

    @Test
    public void DeleteUserByUsernameShouldReturnVoid() {

        /*-------------------------- Given  --------------------------*/
        UserDto userDto = createUser();

        userService.insert(userDto);

        /*-------------------------- When  --------------------------*/

        int usersCount = userService.findAll().size();
        userService.deleteUserByUsername(userDto.getUsername(), userDto.getPassword());

        /*-------------------------- Then  --------------------------*/

        assertTrue(usersCount - 1 == userService.findAll().size());


    }

    @Test
    public void DeleteAllShouldReturnVoid() {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = createUser();

        userService.insert(userDto);


        UserDto userDto2 = createUser();

        userService.insert(userDto2);

        /*-------------------------- When  --------------------------*/

        int usersCount = userService.findAll().size();
        userService.deleteAll();

        /*-------------------------- Then  --------------------------*/

        assertTrue(userService.findAll().size() == 0);
        assertTrue(usersCount > 0);

    }

    @Test
    public void findedByUsernameShouldReturnBoolean() {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = createUser();

        userService.insert(userDto);


        /*-------------------------- When  --------------------------*/

        Boolean userFindedTrue = userService.usernameIsValid("username99");
        Boolean userFindedFalse = userService.usernameIsValid(userDto.getUsername());

        /*-------------------------- Then  --------------------------*/

        assertTrue(userFindedTrue);
        assertFalse(userFindedFalse);

    }


    private UserDto createUser() {

        UserDto userDto = new UserDto();


        userDto.setId(Id);
        userDto.setUsername("TestUsername" + Id);
        userDto.setPassword(Base64.getMimeEncoder().encodeToString("12345678".getBytes()));
        userDto.setActive(true);
        userDto.setRedmineUser("TestUser" + Id);
        userDto.setRedmineKey("asdadadsadadsada");

        Id++;

        return userDto;
    }

    @Override
    protected void initializeIntegrationTest() {

    }
}
