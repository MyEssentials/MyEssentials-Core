package myessentials.exception;

import myessentials.localization.api.LocalManager;
import net.minecraft.util.text.ITextComponent;

public abstract class FormattedException extends RuntimeException {

    public final ITextComponent message;

    public FormattedException(String localizationKey, Object... args) {
        message = LocalManager.get(localizationKey, args);
    }
}
