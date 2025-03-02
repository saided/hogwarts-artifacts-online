package com.example.hogwarts_artifacts_online.system.exceptions;

import com.example.hogwarts_artifacts_online.artifact.ArtifactNotFoundException;
import com.example.hogwarts_artifacts_online.system.Result;
import com.example.hogwarts_artifacts_online.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArtifactNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handleArtifactNotFound(ArtifactNotFoundException ex){
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        System.out.println("Errors:"+ errors.toString());
        Map<String,String> mp = new HashMap<>(errors.size());
        errors.forEach(error -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            mp.put(key,val);
        });

        return  new Result(false, StatusCode.INVALID_ARGUMENT,
                "Provided arguments are invalid, see data for details.",mp);

    }
}
