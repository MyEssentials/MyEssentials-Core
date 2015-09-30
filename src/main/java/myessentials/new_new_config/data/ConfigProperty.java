package myessentials.new_new_config.data;

import myessentials.MyEssentialsCore;
import myessentials.new_new_config.ConfigField;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class ConfigProperty {

    public final Field field;
    public final ConfigField annotation;
    public final Object instance;

    public ConfigProperty(Field field, ConfigField annotation, Object instance) {
        this.field = field;
        this.annotation = annotation;
        this.instance = instance;
    }

    public void set(Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException ex) {
            MyEssentialsCore.instance.LOG.error("Failed to set field {}", field.getName());
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    public Object get() {
        try {
            return field.get(instance);
        } catch (IllegalAccessException ex) {
            MyEssentialsCore.instance.LOG.error("Failed to get field {}", field.getName());
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
            return null;
        }
    }

    public String getAsString() {
        return get().toString();
    }

    public String[] getAsStringArray() {
        Object values = get();
        int length = Array.getLength(values);
        String[] stringArray = new String[length];

        for (int i = 0; i < length; i++) {
            stringArray[i] = Array.get(values, i).toString();
        }
        return stringArray;
    }

    public Object[] getAsArray() {
        Object values = get();
        int length = Array.getLength(values);
        Object[] objectArray = new String[length];

        for (int i = 0; i < length; i++) {
            objectArray[i] = Array.get(values, i);
        }
        return objectArray;
    }

    public Class<?> getType() {
        return field.getType();
    }
}
