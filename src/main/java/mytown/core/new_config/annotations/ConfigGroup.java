package mytown.core.new_config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigGroup {
    /**
     * Returns the name of the category.
     * If set to empty string or null, name will be name of the class its attached to.
     *
     * @return
     */
    String name() default "";

    /**
     * Returns the comment of the category.
     *
     * @return Comment of the category
     */
    String comment() default "";

    Class<?>[] classes() default {};
}
