package br.com.megahack.api.controllers;

import br.com.megahack.api.errors.ApiError;
import br.com.megahack.api.errors.ApiSubError;
import br.com.megahack.api.errors.ApiValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class StandardExceptionHandler extends ResponseEntityExceptionHandler {

    Logger log = LoggerFactory.getLogger(StandardExceptionHandler.class);

    @ExceptionHandler(value={HttpClientErrorException.class})
    protected ResponseEntity<ApiError> handleUserBasedAuthenticationAccessException(HttpClientErrorException ex, WebRequest request){
        logException(ex);
        ApiError error = null;
        //This is the default handling for every Http Error
        error = new ApiError(ex.getStatusCode(), ex.getMessage());
        return new ResponseEntity<>(error, ex.getStatusCode());
    }

    @ExceptionHandler(value={AccessDeniedException.class})
    protected ResponseEntity<ApiError> handlerAccessDeniedException(AccessDeniedException ex, WebRequest request){
        logException(ex);
        return new ResponseEntity<>(new ApiError(HttpStatus.FORBIDDEN, ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ApiSubError> errors= new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            ApiValidationError e = new ApiValidationError();
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            e.setField(fieldName);
            e.setMessage(errorMessage);
            e.setRejectedValue(((FieldError) error).getRejectedValue());
            e.setObject(error.getObjectName());
            errors.add(e);
        });

        ApiError apiError = new ApiError();
        apiError.setSubErrors(errors);
        apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Error while validating fields");

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> response(Exception ex, WebRequest request, HttpStatus status,
                                            String message, HttpHeaders headers) {
        return handleExceptionInternal(ex, message, headers, status, request);
    }

    @ExceptionHandler(value = Exception.class)
    protected  ResponseEntity<ApiError> handlerAnyException(Exception ex, WebRequest request){
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        log.error("{}", error);
        log.error("{}", ex.getStackTrace());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private void logException(Exception ex){
        log.error("{}", ex.getStackTrace());
    }
}
