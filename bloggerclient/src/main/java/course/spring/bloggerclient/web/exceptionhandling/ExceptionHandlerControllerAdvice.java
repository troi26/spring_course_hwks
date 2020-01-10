package course.spring.bloggerclient.web.exceptionhandling;

import course.spring.bloggerclient.exception.InvalidEntityException;
import course.spring.bloggerclient.exception.NonexisitngEntityException;
import course.spring.bloggerclient.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("course.spring.bloggerclient.web")
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionsHandler (NonexisitngEntityException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(HttpStatus.NOT_FOUND,
                        ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionsHandler (InvalidEntityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}
