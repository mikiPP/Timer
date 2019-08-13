package es.sm2baleares.base.model.api.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CreateUserDto {


    @ApiModelProperty(value = "username", required = true, example = "solid.snake", position = 10)
    @NotNull
    @NotEmpty
    private String username;

    @ApiModelProperty(value = "password", required = true, example = "abcdef", position = 20)
    @NotNull
    @NotEmpty
    private String password;

    @ApiModelProperty(value = "redmine User", example = "solid.snake.sm2", position = 30)
    private String redmineUser;

    @ApiModelProperty(value = "redmine Key", example = "5226acff2066addc42442bd36c444f246a762c32", position = 40)
    private String redmineKey;

    @ApiModelProperty(example = "true", value = "User is active", position = 50)
    private Boolean active;

}
