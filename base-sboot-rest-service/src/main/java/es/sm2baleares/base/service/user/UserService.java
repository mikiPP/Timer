package es.sm2baleares.base.service.user;

import es.sm2baleares.base.model.api.user.UserDto;
import es.sm2baleares.spring.common.service.common.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends BaseService<Long, UserDto>, UserDetailsService {


}
