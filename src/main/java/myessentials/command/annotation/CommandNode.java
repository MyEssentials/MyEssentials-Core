package myessentials.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandNode {
    String name();

    String permission();

    String parentName() default "ROOT";

    String syntax() default "";

    String description() default "";

    String[] alias() default {};

    boolean nonPlayers() default false;

    boolean players() default true;

    String[] completionKeys() default {};
}
