package in.org.rebit.exception;

import in.org.rebit.dto.ErrorTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class ExceptionDetails {

    private Date timestamp;
    private String errorDescription;
    private int responseStatusCode;
    private ErrorTypeEnum errorType;
    private String errorCode;

    public ExceptionDetails() {

    }

    public ExceptionDetails(Date timestamp, ErrorTypeEnum errorType, int responseStatusCode, String errorCode,
                            String errorDescription) {
        this.timestamp = timestamp;
        this.errorDescription = errorDescription;
        this.responseStatusCode = responseStatusCode;
        this.errorType = errorType;
        this.errorCode = errorCode;
    }
}