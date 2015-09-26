package MAS.Exception;

public class IntegrityConstraintViolationException extends Exception {
    public IntegrityConstraintViolationException() {
        super();
    }
    public IntegrityConstraintViolationException(String message) {
        super(message);
    }
}
