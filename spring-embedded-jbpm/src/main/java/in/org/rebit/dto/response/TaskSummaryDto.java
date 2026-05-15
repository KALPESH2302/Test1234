package in.org.rebit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.task.model.TaskSummary;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskSummaryDto {

    private List<TaskSummary> taskSummary;
}
