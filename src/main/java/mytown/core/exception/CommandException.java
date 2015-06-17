package mytown.core.exception;

/**
 * Exception thrown when something related to commands failed
 */
public class CommandException extends RuntimeException {
    public CommandException() {

    }

    public CommandException(Throwable cause) {
        this.initCause(cause);
    }
}
