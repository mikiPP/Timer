package es.sm2baleares.base.service.user;

import es.sm2baleares.base.model.api.user.UserDto;
import es.sm2baleares.base.model.domain.User;
import es.sm2baleares.base.repository.UserRepository;
import es.sm2baleares.base.service.user.converter.UserConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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


    @Override
    public List<UserDto> findAll() {

        return userConverter.toApiModel(userRepository.findAll(), UserDto.class);
    }

    @Override
    public Optional<UserDto> findOne(Long id) {
        return Optional.of(userConverter.toApiModel(userRepository.findById(id).get(), UserDto.class));
    }

    @Override
    public UserDto insert(UserDto userDto) {

        User user = userConverter.toDomainModel(userDto, User.class);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        userRepository.save(user);

        return userConverter.toApiModel(user, UserDto.class);
    }

    @Override
    public UserDto update(UserDto userDto) {

        User user = userRepository.findById(userDto.getId()).get();

        user.setUsername(userDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(userDto.getActive());

        userRepository.save(user);

        return userConverter.toApiModel(user, UserDto.class);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepository.findByUsername(username);

        if (user != null) {

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), emptyList());
        }

        throw new UsernameNotFoundException(user.getUsername());
    }
}
