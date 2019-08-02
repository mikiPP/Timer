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
public class AuthUserDto {

    @ApiModelProperty(value = "old username", required = true, example = "solid.snake", position = 20)
    @NotNull
    @NotEmpty
    private String oldUsername;

    @ApiModelProperty(value = "old password", required = true, example = "abcdef", position = 30)
    @NotNull
    @NotEmpty
    private String oldPassword;


    @ApiModelProperty(value = "new username", required = true, example = "solid.snake NEW", position = 40)
    private String newUsername;

    @ApiModelProperty(value = "new password", required = true, example = "abcdef NEW", position = 50)
    private String newPassword;

    @ApiModelProperty(example = "true", value = "User is active", position = 60)
    private Boolean active;


}
