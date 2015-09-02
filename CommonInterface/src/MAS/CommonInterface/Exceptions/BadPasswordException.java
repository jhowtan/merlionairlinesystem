package MAS.CommonInterface.Exceptions;

public class BadPasswordException extends Exception {
    public BadPasswordException() {
        super();
    }
    public BadPasswordException(String message) {
        super(message);
    }
}