package in.org.rebit.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    ExceptionResponseEntityHandler exceptionHandle;

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Object> exceptionConfig(CustomException customException) {
        return exceptionResponseHandler(customException);
    }

    public ResponseEntity<Object> exceptionResponseHandler(CustomException customException) {
        logger.error("Exception occurred in " + customException);
        logger.error(customException.getMessage(), customException);
        return exceptionHandle.responseHandler(customException);
    }
}