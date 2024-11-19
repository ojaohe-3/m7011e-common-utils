package exceptions;

import lombok.Getter;

@Getter
public abstract class AbstractVendorError extends RuntimeException {

    protected ErrorMessage errorMessage;

    public AbstractVendorError(String message) {
        super(message);
    }
}
