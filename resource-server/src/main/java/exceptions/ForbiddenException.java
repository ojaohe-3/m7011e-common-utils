package exceptions;

public class ForbiddenException extends AbstractVendorError {
    public ForbiddenException(String message) {
        super(message);
    }
}
