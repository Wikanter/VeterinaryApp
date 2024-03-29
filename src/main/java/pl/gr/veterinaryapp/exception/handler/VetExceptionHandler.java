package pl.gr.veterinaryapp.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.gr.veterinaryapp.exception.IncorrectDataException;
import pl.gr.veterinaryapp.exception.ResourceNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class VetExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = IncorrectDataException.class)
    protected ResponseEntity<Message> incorrectData(
            IncorrectDataException ex, WebRequest request) {
        log.error("Incorrect data provided", ex);
        String path = request.getDescription(false);
        Message errorDetails = new Message(HttpStatus.BAD_REQUEST, ex.getMessage(), path, LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, new HttpHeaders(), errorDetails.getHttpStatus());
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    protected ResponseEntity<Message> resourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found", ex);
        String path = request.getDescription(false);
        Message errorDetails = new Message(HttpStatus.NOT_FOUND, ex.getMessage(), path, LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, new HttpHeaders(), errorDetails.getHttpStatus());
    }
}
