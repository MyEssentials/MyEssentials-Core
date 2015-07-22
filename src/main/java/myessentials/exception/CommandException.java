package myessentials.exception;

/**
 * Exception thrown when something related to commands failed
 */
public class CommandException extends RuntimeException {
    public CommandException() {

    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Throwable cause) {
        this.initCause(cause);
    }
}
