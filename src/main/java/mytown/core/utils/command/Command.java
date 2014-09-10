package mytown.core.utils.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command annotation, does NOT have a parent and is registered
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name();

    String permission();

    String syntax() default "";

    String description() default "";

    String[] alias() default {};

    boolean console() default false;

    boolean rcon() default false;

    boolean commandblocks() default false;

    String[] completionKeys() default {""};
}
