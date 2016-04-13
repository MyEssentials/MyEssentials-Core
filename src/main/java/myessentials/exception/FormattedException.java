package myessentials.exception;

import org.spongepowered.api.text.Text;

public abstract class FormattedException extends RuntimeException {

    public final Text message;

    public FormattedException(Text messsage) {
        this.message = messsage;
    }
}
