package peaksoft.exceptions;

public class BadCredentialException extends RuntimeException{
    public BadCredentialException() {
        super();
    }

    public BadCredentialException(String message) {
        super(message);
    }
}
