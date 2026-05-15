package in.org.rebit.jbpm.services;

import in.org.rebit.dto.request.TaskLifeCycleDto;
import in.org.rebit.dto.response.TaskSummaryDto;
import in.org.rebit.model.BpmnFiles;
import in.org.rebit.repository.BpmnFileRepository;
import jakarta.transaction.Transactional;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Transactional
@Component
public class TaskLifeCycleImpl {

    private static final Logger logger = LoggerFactory.getLogger(TaskLifeCycleImpl.class);

    private final BpmnInitializerService bpmnInitializerService;

    private final BpmnFileRepository bpmnFileRepository;

    public TaskLifeCycleImpl(BpmnInitializerService bpmnInitializerService, BpmnFileRepository bpmnFileRepository) {
        this.bpmnInitializerService = bpmnInitializerService;
        this.bpmnFileRepository = bpmnFileRepository;
    }

    public synchronized ProcessInstance createProcessInstance(TaskLifeCycleDto taskLifeCycleDto) {
        RuntimeEngine engine = bpmnInitializerService.getRuntimeEngine();
        try {
            ProcessInstance processInstance = bpmnInitializerService.getKieSession(engine).startProcess(taskLifeCycleDto.getProcessId());
            logger.info("Process Instance Initiated with id: " + processInstance.getId() + " for process: " + taskLifeCycleDto.getProcessId());
            return processInstance;

        } finally {
            bpmnInitializerService.disposeRuntimeEngine(engine);
        }
    }

    public synchronized TaskSummaryDto getTaskSummary(TaskLifeCycleDto taskLifeCycleDto) {
        RuntimeEngine engine = bpmnInitializerService.getRuntimeEngine();
        try {
            TaskService taskService = bpmnInitializerService.getTaskService(engine);
            List<Status> statusList = Arrays.asList(Status.Created, Status.Reserved, Status.Ready);
            List<TaskSummary> taskSummaries = taskService.getTasksByStatusByProcessInstanceId(taskLifeCycleDto.getProcessInstanceId(), statusList, "en");

            if (!taskSummaries.isEmpty()) {
                logger.info("Task Summary obtained for task id: " + taskSummaries.get(0).getId() + " & current task status is: " + taskSummaries.get(0).getStatus());
            }
            return new TaskSummaryDto(taskSummaries);

        } finally {
            bpmnInitializerService.disposeRuntimeEngine(engine);
        }
    }

    public synchronized void startTask(TaskLifeCycleDto taskLifeCycleDto) {
        RuntimeEngine engine = bpmnInitializerService.getRuntimeEngine();
        try {
            TaskService taskService = bpmnInitializerService.getTaskService(engine);
            taskService.start(taskLifeCycleDto.getTaskId(), taskLifeCycleDto.getUser());

            List<Status> statusList = List.of(Status.InProgress);
            List<TaskSummary> taskSummaries = taskService.getTasksByStatusByProcessInstanceId(taskLifeCycleDto.getProcessInstanceId(), statusList, "en");
            logger.info("Task started with task id: " + taskSummaries.get(0).getId() + " & current task status is: " + taskSummaries.get(0).getStatus());

        } finally {
            bpmnInitializerService.disposeRuntimeEngine(engine);
        }
    }

    public synchronized void claimTask(TaskLifeCycleDto taskLifeCycleDto) {
        RuntimeEngine engine = bpmnInitializerService.getRuntimeEngine();
        try {
            TaskService taskService = bpmnInitializerService.getTaskService(engine);
            taskService.claim(taskLifeCycleDto.getTaskId(), taskLifeCycleDto.getUser());

            List<Status> statusList = Arrays.asList(Status.Created, Status.Reserved, Status.Ready);
            List<TaskSummary> taskSummaries = taskService.getTasksByStatusByProcessInstanceId(taskLifeCycleDto.getProcessInstanceId(), statusList, "en");
            logger.info("Task claimed with task id: " + taskSummaries.get(0).getId() + " & current task status is: " + taskSummaries.get(0).getStatus());

        } finally {
            bpmnInitializerService.disposeRuntimeEngine(engine);
        }
    }

    public synchronized void activateTask(TaskLifeCycleDto taskLifeCycleDto) {
        RuntimeEngine engine = bpmnInitializerService.getRuntimeEngine();
        try {
            TaskService taskService = bpmnInitializerService.getTaskService(engine);
            taskService.activate(taskLifeCycleDto.getTaskId(), taskLifeCycleDto.getUser());

            List<Status> statusList = Arrays.asList(Status.Created, Status.Reserved, Status.Ready);
            List<TaskSummary> taskSummaries = taskService.getTasksByStatusByProcessInstanceId(taskLifeCycleDto.getProcessInstanceId(), statusList, "en");
            logger.info("Task activated with task id: " + taskSummaries.get(0).getId() + " & current task status is: " + taskSummaries.get(0).getStatus());

        } finally {
            bpmnInitializerService.disposeRuntimeEngine(engine);
        }
    }

    public synchronized void completeTask(TaskLifeCycleDto taskLifeCycleDto) {
        RuntimeEngine engine = bpmnInitializerService.getRuntimeEngine();
        try {
            TaskService taskService = bpmnInitializerService.getTaskService(engine);
            taskService.complete(taskLifeCycleDto.getTaskId(), taskLifeCycleDto.getUser(), taskLifeCycleDto.getGatewayVar());

            List<Status> statusList = List.of(Status.Completed);
            List<TaskSummary> taskSummaries = taskService.getTasksByStatusByProcessInstanceId(taskLifeCycleDto.getProcessInstanceId(), statusList, "en");
            logger.info("Task completed with task id: " + taskSummaries.get(taskSummaries.size() - 1).getId() + " & current task status is: " + taskSummaries.get(taskSummaries.size() - 1).getStatus());

        } finally {
            bpmnInitializerService.disposeRuntimeEngine(engine);
        }
    }

    public synchronized void addBusinessProcess(MultipartFile bpmnFile, String processId) throws IOException {
        BpmnFiles bpmnFiles = new BpmnFiles();
        bpmnFiles.setFileContent(new String(bpmnFile.getBytes(), StandardCharsets.UTF_8));
        bpmnFiles.setProcessId(processId);
        bpmnFileRepository.save(bpmnFiles);
        rebuild();
    }

    public synchronized void rebuild() {
        bpmnInitializerService.closeRuntimeManager();
        bpmnInitializerService.initRuntimeManager();
    }
}
