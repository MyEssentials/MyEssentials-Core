package mytown.core.utils.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by AfterWind on 8/28/2014.
 * CommandNode annotation, has parent
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandNode {
    String name();

    String permission();

    String parentName();

    String syntax() default "";

    String description() default "";

    String[] alias() default {};

    boolean nonPlayers() default false;

    boolean players() default true;

    String[] completionKeys() default {""};
}
