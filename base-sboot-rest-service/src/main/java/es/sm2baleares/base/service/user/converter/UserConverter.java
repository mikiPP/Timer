package es.sm2baleares.base.service.user.converter;

import es.sm2baleares.base.model.api.user.UserDto;
import es.sm2baleares.base.model.domain.User;
import es.sm2baleares.spring.common.service.common.AbstractModelConverter;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends AbstractModelConverter<UserDto, User> {


}
