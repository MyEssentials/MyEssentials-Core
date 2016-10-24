package myessentials.exception;

/**
 * Exception thrown when something related to economy failed
 */
public class EconomyException extends RuntimeException {
    public EconomyException(String message) {
        super(message);
    }

    public EconomyException(String message, Throwable cause) {
        super(message, cause);
    }
}
