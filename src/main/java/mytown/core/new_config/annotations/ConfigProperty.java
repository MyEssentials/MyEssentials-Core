package mytown.core.new_config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigProperty {
    /**
     * Returns the name of the property.
     * If set to empty string or null, name will be name of the field its attached to.
     *
     * @return Name of property
     */
    String name() default "";

    /**
     * Returns the comment of the property.
     *
     * @return Comment of the property
     */
    String comment() default "";

    /**
     * Returns if this property can be changed by commands.
     *
     * @return Can be changed by commands
     */
    boolean command() default true;
}
