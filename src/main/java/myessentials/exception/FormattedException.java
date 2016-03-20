package myessentials.exception;

import myessentials.localization.api.LocalManager;
import net.minecraft.util.IChatComponent;

public abstract class FormattedException extends RuntimeException {

    public final IChatComponent message;

    public FormattedException(String localizationKey, Object... args) {
        message = LocalManager.get(localizationKey, args);
    }
}
