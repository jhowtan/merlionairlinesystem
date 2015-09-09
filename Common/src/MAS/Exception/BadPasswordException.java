package MAS.Exception;

public class BadPasswordException extends Exception {
    public BadPasswordException() {
        super();
    }
    public BadPasswordException(String message) {
        super(message);
    }
}