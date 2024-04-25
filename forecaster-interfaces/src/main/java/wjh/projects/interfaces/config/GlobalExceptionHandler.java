package wjh.projects.interfaces.config;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wjh.projects.common.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMessage handle(MethodArgumentNotValidException exception) {
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        List<Map<String, Object>> data = new ArrayList<>();
        for (ObjectError error : errors) {
            Map<String, Object> map = new HashMap<>();
            FieldError fieldError = (FieldError) error;
            map.put("defaultMessage", fieldError.getDefaultMessage());
            map.put("objectName", fieldError.getObjectName());
            map.put("field", fieldError.getField());
            map.put("rejectedValue", fieldError.getRejectedValue());
            map.put("code", fieldError.getCode());
            data.add(map);
        }
        return new ResponseMessage(ResponseCodeEnum.METHOD_ARGUMENT_NOT_VALID, data);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseMessage handle(ResourceNotFoundException exception) {
        return new ResponseMessage(ResponseCodeEnum.RESOURCE_NOT_FOUND, exception.getMessage());
    }
}
