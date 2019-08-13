package es.sm2baleares.base.model.api.task;

import es.sm2baleares.base.model.api.user.UserDto;
import es.sm2baleares.spring.common.model.api.common.ApiModelItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@ApiModel
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class TaskDto extends ApiModelItem {

    @ApiModelProperty(example = "1", value = "task ID", required = true)
    private Long id;

    @ApiModelProperty(example = "MyName", value = "task's name", required = true, position = 10)
    @NotEmpty
    @NotNull
    private String name;

    @ApiModelProperty(value = "User's id", required = true, position = 20)
    private UserDto user;

    @ApiModelProperty(example = "MyDescription", value = "task's desciption", required = true, position = 30)
    private String description;

    @ApiModelProperty(example = "2019-07-18T09:55:20", value = "When task started", position = 40)
    private LocalDateTime start_time;

    @ApiModelProperty(example = "2019-07-18T09:55:20", value = "When task ended", position = 50)
    private LocalDateTime end_time;

    @ApiModelProperty(example = "1234", value = "task's duration", position = 60)
    private Integer duration;

    @ApiModelProperty(example = "miliseconds", value = "unit of time", required = true, position = 70)
    private String time_in;

    @ApiModelProperty(example = "true", value = "User is active", position = 80)
    private Boolean active;
}
