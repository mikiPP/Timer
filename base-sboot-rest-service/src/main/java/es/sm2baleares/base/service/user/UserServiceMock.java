package es.sm2baleares.base.service.user;

import com.google.common.base.Preconditions;
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
    public void setUp() {

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


    @Override
    public List<UserDto> findAll() {
        return userConverter.toApiModel(users, UserDto.class);
    }

    @Override
    public Optional<UserDto> findOne(Long id) {
        return Optional.of(userConverter.toApiModel(findById(id), UserDto.class));
    }

    @Override
    public UserDto insert(UserDto userDto) {

        User user = userConverter.toDomainModel(userDto, User.class);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);

        users.add(user);

        return userConverter.toApiModel(user, UserDto.class);
    }

    @Override
    public UserDto update(UserDto userDto) {

        User user = findById(userDto.getId());

        user.setUsername(userDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setActive(userDto.getActive());

        users.add((Math.toIntExact(userDto.getId())),user);

        return userConverter.toApiModel(user, UserDto.class);
    }

    @Override
    public void delete(Long id) {
        users.remove(findById(id));
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = loadByUsername(username);

        if (user != null) {

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), emptyList());
        }

        throw new UsernameNotFoundException(user.getUsername());

    }

    private User findById(Long id) {

        User userToReturn = new User();

        for (User user : users) {

            if (user.getId().equals(id)) {
                userToReturn = user;
                break;
            }
        }

        if (userToReturn.getId() == null) {
             throw new NotFoundException("User with id: " +id + " don't exists");
        }

        return userToReturn;
    }


    private User loadByUsername(String username) throws UsernameNotFoundException {

        User userToReturn = new User();

        for (User user : users) {

            if (user.getUsername().equals(username)) {
                userToReturn = user;
                break;
            }
        }

        if (userToReturn.getUsername() == null) {
            throw new NotFoundException("User with id: " + username + " don't exists");
        }

        return userToReturn;
    }
}
