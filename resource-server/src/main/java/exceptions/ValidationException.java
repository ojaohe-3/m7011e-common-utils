package exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class ValidationException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public ValidationException(Errors errors) {
        super("Validation failed");
        HashMap<String, String> fieldErrors = new HashMap<>();
        HashMap<String, String> errorCodes = new HashMap<>();
        HashMap<String, Object> rejectedValues = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
            errorCodes.put(error.getField(), error.getCode());
            rejectedValues.put(error.getField(), error.getRejectedValue());
        }
        errorMessage = new ErrorMessage(fieldErrors, errorCodes, rejectedValues);
    }
}

;
