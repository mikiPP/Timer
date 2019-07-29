package es.sm2baleares.base.model.api.user;

import es.sm2baleares.spring.common.model.api.common.ApiModelItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserDto extends ApiModelItem {

    @ApiModelProperty(value = "User ID", example = "1", position = 10)
    private Long id;

    @ApiModelProperty(value = "username", required = true, example = "solid.snake", position = 20)
    @NotNull
    private String username;

    @ApiModelProperty(value = "password", required = true, example = "abcdef", position = 30)
    @NotNull
    private String password;

    @ApiModelProperty(example = "true", value = "User is active", position = 40)
    private Boolean active;

}
