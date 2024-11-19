package controller;

import exceptions.ForbiddenException;
import exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler
{

    @ExceptionHandler(value = {ForbiddenException.class})
    public void handleForbidden(ForbiddenException ex, WebRequest request){
        handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class, exceptions.ResourceNotFoundException.class})
    public void handleNotFound(ResourceNotFoundException ex, WebRequest request){
        handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {ValidationException.class, jakarta.xml.bind.ValidationException.class})
    public void handleValidation(ValidationException ex, WebRequest request){
        handleExceptionInternal(ex, ex.getErrorMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
