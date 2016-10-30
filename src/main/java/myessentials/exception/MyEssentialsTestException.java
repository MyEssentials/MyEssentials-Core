package myessentials.exception;

/**
 * Exception when something related to testing fails.
 */
public class MyEssentialsTestException extends Exception {

    public MyEssentialsTestException() {
        super();
    }

    public MyEssentialsTestException(String message) {
        super(message);
    }
}
