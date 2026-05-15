package in.org.rebit.exception;

import in.org.rebit.dto.ErrorTypeEnum;
import in.org.rebit.util.ExceptionConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ExceptionResponseEntityHandler {

    public ResponseEntity<Object> responseHandler(CustomException customException) {

        String errorDescription = customException.getMessage();
        int responseStatusCode = ExceptionConstants.RESPONSE_STATUS_CODE;
        ErrorTypeEnum errorType = ErrorTypeEnum.fromString(customException.getErrorType());

        ExceptionDetails error = new ExceptionDetails(new Date(), errorType, responseStatusCode, customException.getErrorCode(),
                errorDescription);

        return new ResponseEntity<Object>(error, new HttpHeaders(), HttpStatus.resolve(responseStatusCode));
    }
}