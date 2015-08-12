package myessentials.new_config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a Config.
 *
 * Not currently used, but there are plans!
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Config {
    /**
     * The filename of the config class
     * @return
     */
    String value() default "";

    /**
     * The configuration backend to load (forge or json)
     * @return
     */
    String backend() default "forge";

    /**
     * Marks a field to hold the ConfigData
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Instance {
    }

    /**
     * Marks a class as a config group
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Group {
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

    /**
     * Marks a field as a config property
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Property {
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
}
