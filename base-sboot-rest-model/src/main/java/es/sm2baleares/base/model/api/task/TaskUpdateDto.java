package es.sm2baleares.base.model.api.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@ApiModel
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class TaskUpdateDto {

    @ApiModelProperty(example = "MyOldName", value = "task's old name", required = true, position = 10)
    @NotEmpty
    @NotNull
    private String oldName;

    @ApiModelProperty(example = "MyNewName", value = "task's new name", required = true, position = 20)
    private String newName;


    @ApiModelProperty(example = "MyNewDescription", value = "task's new desciption", required = true, position = 30)
    private String newDescription;
}
