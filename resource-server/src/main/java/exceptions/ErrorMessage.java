package exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    Map<String, String> fieldErrors;
    Map<String, String> errorCodes;
    Map<String, Object> rejectedValues;
}
