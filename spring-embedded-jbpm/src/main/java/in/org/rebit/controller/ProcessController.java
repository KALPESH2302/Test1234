package in.org.rebit.controller;

import in.org.rebit.dto.request.TaskLifeCycleDto;
import in.org.rebit.dto.response.GenericResponseDto;
import in.org.rebit.dto.response.ProcessInstanceDetailsDto;
import in.org.rebit.dto.response.TaskSummaryDto;
import in.org.rebit.exception.CustomException;
import in.org.rebit.jbpm.services.TaskLifeCycleImpl;
import in.org.rebit.util.Constants;
import in.org.rebit.util.ExceptionConstants;
import org.kie.api.runtime.process.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

    @Autowired
    TaskLifeCycleImpl taskLifeCycleImpl;

    @PostMapping(value = "/add-new-business-process",
            consumes = {"multipart/form-data"})
    public ResponseEntity<GenericResponseDto> addNewBusinessProcess(@RequestHeader("Authorization") String token, @RequestParam("bpmnFile") MultipartFile bpmnFile, @RequestParam("processId") String processId) {
        try {
            taskLifeCycleImpl.addBusinessProcess(bpmnFile, processId);
            return ResponseEntity.ok().body(new GenericResponseDto(true, Constants.BUSINESS_PROCESS_ADDED));

        } catch (Throwable throwable) {
            throw new CustomException(ExceptionConstants.ADD_NEW_BUSINESS_PROCESS_ERROR_DESC,
                    ExceptionConstants.ADD_NEW_BUSINESS_PROCESS_ERROR_CODE,
                    ExceptionConstants.ERROR_TYPE_TECHNICAL);
        }
    }

    @PostMapping("/create-process-instance")
    public ResponseEntity<ProcessInstanceDetailsDto> createProcessInstance(@RequestHeader("Authorization") String token, @RequestBody TaskLifeCycleDto taskLifeCycleDto) {

        try {
            ProcessInstance processInstance = taskLifeCycleImpl.createProcessInstance(taskLifeCycleDto);
            return ResponseEntity.ok(new ProcessInstanceDetailsDto(processInstance.getId()));

        } catch (Throwable throwable) {
            throw new CustomException(ExceptionConstants.CREATE_PROCESS_INSTANCE_ERROR_DESC,
                    ExceptionConstants.CREATE_PROCESS_INSTANCE_ERROR_CODE,
                    ExceptionConstants.ERROR_TYPE_TECHNICAL);
        }
    }

    @PostMapping("/get-task-summary")
    public ResponseEntity<TaskSummaryDto> getTaskSummary(@RequestHeader("Authorization") String token, @RequestBody TaskLifeCycleDto taskLifeCycleDto) {

        try {
            TaskSummaryDto taskSummaryDto = taskLifeCycleImpl.getTaskSummary(taskLifeCycleDto);
            return ResponseEntity.ok().body(taskSummaryDto);

        } catch (Throwable throwable) {
            throw new CustomException(ExceptionConstants.TASK_SUMMARY_ERROR_DESC,
                    ExceptionConstants.TASK_SUMMARY_ERROR_CODE,
                    ExceptionConstants.ERROR_TYPE_TECHNICAL);
        }
    }

    @PostMapping("/start-task")
    public ResponseEntity<GenericResponseDto> startTask(@RequestHeader("Authorization") String token, @RequestBody TaskLifeCycleDto taskLifeCycleDto) {

        try {
            taskLifeCycleImpl.startTask(taskLifeCycleDto);
            return ResponseEntity.ok().body(new GenericResponseDto(true, Constants.TASK_STARTED));
        } catch (Throwable throwable) {
            throw new CustomException(ExceptionConstants.START_TASK_ERROR_DESC,
                    ExceptionConstants.START_TASK_ERROR_CODE,
                    ExceptionConstants.ERROR_TYPE_TECHNICAL);
        }
    }

    @PostMapping("/claim-task")
    public ResponseEntity<GenericResponseDto> claimTask(@RequestHeader("Authorization") String token, @RequestBody TaskLifeCycleDto taskLifeCycleDto) {

        try {
            taskLifeCycleImpl.claimTask(taskLifeCycleDto);
            return ResponseEntity.ok().body(new GenericResponseDto(true, Constants.TASK_CLAIMED));

        } catch (Throwable throwable) {
            throw new CustomException(ExceptionConstants.CLAIM_TASK_ERROR_DESC,
                    ExceptionConstants.CLAIM_TASK_ERROR_CODE,
                    ExceptionConstants.ERROR_TYPE_TECHNICAL);
        }
    }

    @PostMapping("/activate-task")
    public ResponseEntity<GenericResponseDto> activateTask(@RequestHeader("Authorization") String token, @RequestBody TaskLifeCycleDto taskLifeCycleDto) {

        try {
            taskLifeCycleImpl.activateTask(taskLifeCycleDto);
            return ResponseEntity.ok().body(new GenericResponseDto(true, Constants.TASK_ACTIVATED));

        } catch (Throwable throwable) {
            throw new CustomException(ExceptionConstants.ACTIVATE_TASK_ERROR_DESC,
                    ExceptionConstants.ACTIVATE_TASK_ERROR_CODE,
                    ExceptionConstants.ERROR_TYPE_TECHNICAL);
        }
    }

    @PostMapping("/complete-task")
    public ResponseEntity<GenericResponseDto> completeTask(@RequestHeader("Authorization") String token, @RequestBody TaskLifeCycleDto taskLifeCycleDto) {

        try {
            taskLifeCycleImpl.completeTask(taskLifeCycleDto);
            return ResponseEntity.ok().body(new GenericResponseDto(true, Constants.TASK_COMPLETED));

        } catch (Throwable throwable) {
            throw new CustomException(ExceptionConstants.COMPLETE_TASK_ERROR_DESC,
                    ExceptionConstants.COMPLETE_TASK_ERROR_CODE,
                    ExceptionConstants.ERROR_TYPE_TECHNICAL);
        }
    }

    @GetMapping("/reload-business-processes")
    public ResponseEntity<?> rebuild(@RequestHeader("Authorization") String token) {

        try {
            taskLifeCycleImpl.rebuild();
            return ResponseEntity.ok().body(new GenericResponseDto(true, Constants.RELOAD_BPMN_FILES));

        } catch (Throwable throwable) {
            throw new CustomException(ExceptionConstants.RELOAD_BPMN_FILES_ERROR_DESC,
                    ExceptionConstants.RELOAD_BPMN_FILES_ERROR_CODE,
                    ExceptionConstants.ERROR_TYPE_TECHNICAL);
        }
    }
}

