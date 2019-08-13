package es.sm2baleares.base.service.user;

import com.google.common.base.Preconditions;
import es.sm2baleares.base.model.api.user.AuthUserDto;
import es.sm2baleares.base.model.api.user.CreateUserDto;
import es.sm2baleares.base.model.api.user.UserDto;
import es.sm2baleares.base.model.domain.User;
import es.sm2baleares.base.repository.UserRepository;
import es.sm2baleares.base.service.user.converter.UserConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Collections.emptyList;


@Service("UserService")
@ConditionalOnProperty(name = "test.active", havingValue = "false")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserConverter userConverter;

    // CRUD

    //Create

    @Override
    public UserDto insert(UserDto userDto) {

        Preconditions.checkArgument(userDto.getRedmineKey() != null);
        Preconditions.checkArgument(userDto.getRedmineUser() != null);

        User user = userConverter.toDomainModel(userDto, User.class);
        user.setPassword(new String(Base64.getMimeDecoder().decode(userDto.getPassword())));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        userRepository.save(user);

        return userConverter.toApiModel(user, UserDto.class);
    }


    // Read


    @Override
    public Optional<UserDto> findOne(Long id) {
        return Optional.of(userConverter.toApiModel(userRepository.findById(id).get(), UserDto.class));
    }

    @Override
    public Optional<UserDto> findOne(String username) {

        try {

            return Optional.of(userConverter.toApiModel(userRepository.findByUsername(username).get(), UserDto.class));

        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public List<UserDto> findAll() {

        return userConverter.toApiModel(userRepository.findAll(), UserDto.class);
    }


    //Update

    @Override
    public UserDto update(UserDto userDto) {

        User user = userRepository.findById(userDto.getId()).get();

        userRepository.save(user);

        return userConverter.toApiModel(user, UserDto.class);
    }


    @Override
    public UserDto update(AuthUserDto authUserDto) {

        User user = userRepository.findByUsername(authUserDto.getOldUsername()).get();

        //We need to check if the user put the valid credentials,if it is this case we shouldn't change anything
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

        userRepository.save(user);

        return userConverter.toApiModel(user, UserDto.class);

    }


    // Delete

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }


    @Override
    public void deleteUserByUsername(String username, String password) {

        //We need to check if the user put the valid credentials,if it is this case we shouldn't change anything

        User user = userRepository.findByUsername(username).get();

        Preconditions.checkArgument(user != null);
        Preconditions.checkArgument(bCryptPasswordEncoder.matches(new String
                (Base64.getMimeDecoder().decode(password.getBytes())), user.getPassword()));

        userRepository.delete(user);
    }


    // My methods

    @Override
    public boolean usernameIsValid(String username) {

        //Returns true if the user don't exists,if the user exists, will return false

        try {

            return findOne(username).get() == null;

        } catch (NullPointerException e) {
            return true;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Return the user Details just using the username,this method is to make the jwt's token

        User user = this.userRepository.findByUsername(username).get();

        if (user != null) {

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), emptyList());
        }

        throw new UsernameNotFoundException(user.getUsername());
    }

}
