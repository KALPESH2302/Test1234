package in.org.rebit.util;

public class ExceptionConstants {

    public static final String ERROR_TYPE_TECHNICAL = "TECHNICAL";

    public static final int RESPONSE_STATUS_CODE = 400;
    public static final String ADD_NEW_BUSINESS_PROCESS_ERROR_DESC = "Unable to add new business process";

    public static final String ADD_NEW_BUSINESS_PROCESS_ERROR_CODE = "70001";
    public static final String CREATE_PROCESS_INSTANCE_ERROR_DESC = "Unable to create the process instance";

    public static final String CREATE_PROCESS_INSTANCE_ERROR_CODE = "80001";

    public static final String START_TASK_ERROR_DESC = "Unable to start the given task";

    public static final String START_TASK_ERROR_CODE = "80002";

    public static final String CLAIM_TASK_ERROR_DESC = "Unable to claim the given task";

    public static final String CLAIM_TASK_ERROR_CODE = "80003";

    public static final String ACTIVATE_TASK_ERROR_DESC = "Unable to activate the given task";

    public static final String ACTIVATE_TASK_ERROR_CODE = "80004";

    public static final String COMPLETE_TASK_ERROR_DESC = "Unable to complete the given task";

    public static final String COMPLETE_TASK_ERROR_CODE = "80005";

    public static final String RELOAD_BPMN_FILES_ERROR_DESC = "Unable to reload bpmn files";

    public static final String RELOAD_BPMN_FILES_ERROR_CODE = "80006";

    public static final String TASK_SUMMARY_ERROR_DESC = "Unable to fetch the task summary";

    public static final String TASK_SUMMARY_ERROR_CODE = "80006";

    public static final String JWT_TOKEN_EXPIRY_DATE_ABSENT_ERROR_DESC = "JWT token does not have an expiration claim";

    public static final String JWT_TOKEN_EXPIRY_DATE_ABSENT_ERROR_CODE = "30001";

    public static final String JWT_TOKEN_EXPIRED_ERROR_DESC = "JWT token has been expired";

    public static final String JWT_TOKEN_EXPIRED_ERROR_CODE = "30002";

    public static final String INVALID_JWT_TOKEN_ERROR_DESC = "JWT token is either empty or invalid";

    public static final String INVALID_JWT_TOKEN_ERROR_CODE = "30003";

}
