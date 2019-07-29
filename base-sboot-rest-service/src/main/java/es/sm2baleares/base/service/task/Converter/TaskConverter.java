package es.sm2baleares.base.service.task.Converter;

import es.sm2baleares.base.model.api.task.TaskDto;
import es.sm2baleares.base.model.domain.Task;
import es.sm2baleares.spring.common.service.common.AbstractModelConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
public class TaskConverter extends AbstractModelConverter<TaskDto, Task> {

}
