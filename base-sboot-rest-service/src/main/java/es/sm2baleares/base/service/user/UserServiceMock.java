package es.sm2baleares.base.service.user;

import com.google.common.base.Preconditions;
import es.sm2baleares.base.model.api.user.AuthUserDto;
import es.sm2baleares.base.model.api.user.UserDto;
import es.sm2baleares.base.model.domain.User;
import es.sm2baleares.base.service.exception.NotFoundException;
import es.sm2baleares.base.service.user.converter.UserConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service("UserServiceMock")
@ConditionalOnProperty(name = "test.active", havingValue = "true")
@Slf4j
public class UserServiceMock implements UserService {

    @Getter
    private List<User> users;

    @Getter
    private User user;

    @Getter
    private User user2;


    @Autowired
    private UserConverter userConverter;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    private void setUp() {

        users = new ArrayList<>();
        user = new User();
        user2 = new User();

        user.setId(0l);
        user.setUsername("Test");
        user.setPassword("abcdefg");
        user.setActive(true);

        user2.setId(1l);
        user2.setUsername("Test1");
        user2.setPassword("abcdefg");
        user2.setActive(true);

        users.add(user);
        users.add(user2);

    }


    //CRUD

    //Create

    @Override
    public UserDto insert(UserDto userDto) {

        Preconditions.checkArgument(userDto.getRedmineKey() != null);
        Preconditions.checkArgument(userDto.getRedmineUser() != null);

        User user = userConverter.toDomainModel(userDto, User.class);
        user.setPassword(new String(Base64.getMimeDecoder().decode(userDto.getPassword())));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);

        users.add(user);

        return userConverter.toApiModel(user, UserDto.class);
    }

    //Read

    @Override
    public Optional<UserDto> findOne(Long id) {
        return Optional.of(userConverter.toApiModel(findById(id), UserDto.class));
    }

    @Override
    public Optional<UserDto> findOne(String username) {
        return Optional.of(userConverter.toApiModel(loadByUsername(username), UserDto.class));
    }

    @Override
    public List<UserDto> findAll() {
        return userConverter.toApiModel(users, UserDto.class);
    }


    //Update

    @Override
    public UserDto update(UserDto userDto) {

        User user = findById(userDto.getId());

        user.setUsername(userDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setActive(userDto.getActive());

        users.add(user);

        return userConverter.toApiModel(user, UserDto.class);
    }

    @Override
    public UserDto update(AuthUserDto authUserDto) {

        User user = userConverter.toDomainModel(findOne(authUserDto.getOldUsername()).get(), User.class);
        int position = getIndexOf(authUserDto.getOldUsername());
        authUserDto.setOldPassword(new String(Base64.getMimeDecoder().decode(authUserDto.getOldPassword().getBytes())));

        //We need to check if the user put the valid credentials,if it is this case we shouldn't change anything

        Preconditions.checkArgument(user != null);
        Preconditions.checkArgument(bCryptPasswordEncoder.matches(authUserDto.getOldPassword(), user.getPassword()));

        if (authUserDto.getNewUsername() != null && authUserDto.getNewUsername() != "")
            user.setUsername(authUserDto.getNewUsername());

        if (authUserDto.getNewPassword() != null && authUserDto.getNewPassword() != "") {
            authUserDto.setNewPassword(new String(Base64.getMimeDecoder().decode(authUserDto.getNewPassword().getBytes())));
            user.setPassword(bCryptPasswordEncoder.encode(authUserDto.getNewPassword()));
        }

        if (authUserDto.getRedmineKey() != null && authUserDto.getRedmineKey() != "")
            user.setRedmineKey(authUserDto.getRedmineKey());

        if (authUserDto.getActive() != null) user.setActive(authUserDto.getActive());

        UserDto userDto = userConverter.toApiModel(user, UserDto.class);

        users.set(position, user);

        return userDto;
    }

    //Delete

    @Override
    public void delete(Long id) {
        users.remove(findById(id));
    }


    public void deleteUserByUsername(String username, String password) {

        User user = userConverter.toDomainModel(findOne(username).get(), User.class);

        //We need to check if the user put the valid credentials,if it is this case we shouldn't change anything

        Preconditions.checkArgument(user != null);
        Preconditions.checkArgument(bCryptPasswordEncoder.matches(new String
                (Base64.getMimeDecoder().decode(password.getBytes())), user.getPassword()));

        int index = getIndexOf(user.getUsername());

        Preconditions.checkArgument(index != -1);

        users.remove(index);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    // My methods

    /**
     * @return Boolean
     * <p>
     * <p>
     * This method returns true if the user don't exists,if the user exists, will return false
     * @Param String username
     **/

    @Override
    public boolean usernameIsValid(String username) {

        try {

            return findOne(username) == null;

        } catch (NotFoundException e) {
            return true;
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Return the user Details just using the username,this method is to make the jwt's token

        User user = loadByUsername(username);

        if (user != null) {

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), emptyList());
        }

        throw new UsernameNotFoundException(user.getUsername());

    }

    /**
     * @return User
     * @throws NotFoundException This method looks for the user with the id by parameter if this user is founded will be the return
     *                           if not will return null and throw NotFoundException
     * @Param Long id
     **/


    private User findById(Long id) throws NotFoundException {

        User userToReturn = new User();

        for (User user : users) {

            if (user.getId().equals(id)) {
                userToReturn = user;
                break;
            }
        }

        if (userToReturn.getId() == null) {
            throw new NotFoundException("User with id: " + id + " don't exists");
        }

        return userToReturn;
    }


    /**
     * @return User
     * @throws NotFoundException This method looks for the user with the username by parameter if this user is founded will be the return
     *                           if not will return null and throw NotFoundException
     * @Param String username
     **/

    private User loadByUsername(String username) throws NotFoundException {

        User userToReturn = new User();

        for (User user : users) {

            if (user.getUsername().equals(username)) {
                userToReturn = user;
                break;
            }
        }

        if (userToReturn.getUsername() == null) {
            throw new NotFoundException("User with username: " + username + " don't exists");
        }

        return userToReturn;
    }

    /**
     * @return int
     * @throws NotFoundException This method looks for the user with the username by parameter if this user is founded will return his position
     *                           if not will return -1 and throw NotFoundException
     * @Param String username
     **/


    private int getIndexOf(String username) throws NotFoundException {

        int index = -1;

        for (User user : users) {

            if (user.getUsername().equals(username)) {
                index = users.indexOf(user);
                break;
            }
        }

        if (index == -1) {
            throw new NotFoundException("User with username: " + username + " don't exists");
        }

        return index;
    }

}
