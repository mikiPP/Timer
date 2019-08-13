package es.sm2baleares.base.service.user;

import es.sm2baleares.base.model.api.user.AuthUserDto;
import es.sm2baleares.base.model.api.user.CreateUserDto;
import es.sm2baleares.base.model.api.user.UserDto;
import es.sm2baleares.spring.common.service.common.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface UserService extends BaseService<Long, UserDto>, UserDetailsService {

    void deleteUserByUsername(String username, String password);

    Optional<UserDto> findOne(String username);

    UserDto update(AuthUserDto userDto);

    boolean usernameIsValid(String username);

}
