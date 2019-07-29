package es.sm2baleares.base.service.user;

import es.sm2baleares.base.IntegrationTest;
import es.sm2baleares.base.model.api.user.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


@RunWith( SpringRunner.class )
public class UserServiceTest extends IntegrationTest {


    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;




    @Test
    public void InsertOneUserShouldReturnTheUser() {


        /*-------------------------- Given  --------------------------*/

        UserDto userDto = new UserDto();
        userDto.setId(4l);
        userDto.setUsername("username4");
        userDto.setPassword("password4");


        /*-------------------------- When  --------------------------*/

        int users = userService.findAll().size();

        UserDto userInserted = userService.insert(userDto);

        /*-------------------------- Then  --------------------------*/

        assertTrue(userInserted.getActive());

        assertTrue(userInserted.getUsername().equals(userDto.getUsername()));

        assertTrue(userDto.getPassword() != userInserted.getPassword());

        assertTrue(bCryptPasswordEncoder.matches(userDto.getPassword(),userInserted.getPassword()));

        assertTrue(users+1 == userService.findAll().size());
    }


    @Test
    public void findOneShouldReturnOneOptionalUserDto() {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = new UserDto();
        userDto.setId(5l);
        userDto.setUsername("username5");
        userDto.setPassword("password5");
        userDto.setActive(true);

        userService.insert(userDto);

        /*-------------------------- When  --------------------------*/

        Optional<UserDto> userFinded = userService.findOne(5l);

        /*-------------------------- Then  --------------------------*/

        assertTrue(userFinded instanceof Optional);

        assertTrue(userFinded.get() instanceof  UserDto);

        assertEquals(userDto.getId(),userFinded.get().getId());
        assertEquals(userDto.getUsername(),userFinded.get().getUsername());
        assertEquals(userDto.getActive(),userFinded.get().getActive());

        assertTrue(bCryptPasswordEncoder.matches(userDto.getPassword(),userFinded.get().getPassword()));



    }


    @Test
    public void findAllShouldReturnOneList() {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = new UserDto();
        userDto.setId(6l);
        userDto.setUsername("username6");
        userDto.setPassword("password6");

        userService.insert(userDto);

        UserDto userDto2 = new UserDto();
        userDto2.setId(7l);
        userDto2.setUsername("username7");
        userDto2.setPassword("password7");

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

        UserDto userDto = new UserDto();
        userDto.setId(7l);
        userDto.setUsername("username7");
        userDto.setPassword("password7");
        userDto.setActive(true);

        userService.insert(userDto);

        UserDto userDtoToUpdate = new UserDto();
        userDtoToUpdate.setId(7l);
        userDtoToUpdate.setUsername("username7 updated");
        userDtoToUpdate.setPassword("password7 updated");
        userDtoToUpdate.setActive(false);

        /*-------------------------- When  --------------------------*/

        userService.update(userDtoToUpdate);

        UserDto userUpdated = userService.findOne(7l).get();

        System.out.println(userDtoToUpdate);

        /*-------------------------- Then  --------------------------*/


        assertTrue(userUpdated instanceof UserDto);

        assertTrue(userDto.getUsername() != userUpdated.getUsername()
                && userUpdated.getUsername().equals(userDtoToUpdate.getUsername()));

        assertTrue(! bCryptPasswordEncoder.matches(userDto.getPassword(),userUpdated.getPassword())
                && bCryptPasswordEncoder.matches(userDtoToUpdate.getPassword(),userUpdated.getPassword()));

        System.out.println(userDto.getActive() != userUpdated.getActive());
        System.out.println(userDtoToUpdate.getActive() == userUpdated.getActive());

        assertTrue(userDto.getActive() != userUpdated.getActive()
                && userDtoToUpdate.getActive() == userUpdated.getActive());


    }


    @Test
    public void DeleteShouldReturnVoid() {

        /*-------------------------- Given  --------------------------*/
        UserDto userDto = new UserDto();
        userDto.setId(8l);
        userDto.setUsername("username8");
        userDto.setPassword("password8");
        userDto.setActive(true);

        userService.insert(userDto);

        /*-------------------------- When  --------------------------*/

        int usersCount = userService.findAll().size();
        userService.delete(8l);

        /*-------------------------- Then  --------------------------*/

        assertTrue(usersCount - 1 == userService.findAll().size());


    }

    @Test
    public void DeleteAllShouldReturnVoid() {

        /*-------------------------- Given  --------------------------*/

        UserDto userDto = new UserDto();

        userDto.setId(12l);
        userDto.setUsername("Username12");
        userDto.setPassword("Password12");
        userDto.setActive(true);

        userService.insert(userDto);


        UserDto userDto2 = new UserDto();
        userDto2.setId(13l);
        userDto2.setUsername("Username13");
        userDto2.setPassword("Password13");
        userDto2.setActive(false);

        userService.insert(userDto);

        /*-------------------------- When  --------------------------*/

        int usersCount = userService.findAll().size();
        userService.deleteAll();

        /*-------------------------- Then  --------------------------*/

        assertTrue(userService.findAll().size() == 0);
        assertTrue(usersCount > 0);

    }

    @Override
    protected void initializeIntegrationTest() {

    }
}
