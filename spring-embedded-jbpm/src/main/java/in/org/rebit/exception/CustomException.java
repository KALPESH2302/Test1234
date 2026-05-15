package in.org.rebit.exception;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    static Logger logger = LoggerFactory.getLogger(CustomException.class);
    private String errorCode;
    private String errorType;
    private int responseStatusCode;
    private Throwable throwable;

    public CustomException(String errorMsg, String errorCode, String errorType) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorType = errorType;
    }

    public CustomException(String errorMsg, String errorCode, String errorType, int responseStatusCode) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.responseStatusCode = responseStatusCode;
    }

    public CustomException(String errorMsg, String errorCode, String errorType, Throwable throwable) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.throwable = throwable;
    }
}