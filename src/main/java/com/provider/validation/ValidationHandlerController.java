package com.provider.validation;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ValidationHandlerController {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    List<Violation> onConstraintValidationException(ConstraintViolationException e) {
        List<Violation> violationList = new ArrayList<>();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            violationList.add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return violationList;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    List<Violation> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<Violation> violationList = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            violationList.add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return violationList;
    }
}
