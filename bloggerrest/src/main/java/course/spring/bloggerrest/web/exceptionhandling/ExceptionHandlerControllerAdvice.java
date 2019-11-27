package course.spring.bloggerrest.web.exceptionhandling;

import course.spring.bloggerrest.exception.InvalidEntityException;
import course.spring.bloggerrest.exception.NonexisitngEntityException;
import course.spring.bloggerrest.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice("course.spring.bloggerrest.web")
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
