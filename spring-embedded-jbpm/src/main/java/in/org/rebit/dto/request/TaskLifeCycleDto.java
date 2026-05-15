package in.org.rebit.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class TaskLifeCycleDto {

    private Long processInstanceId;

    private Long taskId;

    private String user;

    private String processId;

    private Map<String, Object> gatewayVar;

}
