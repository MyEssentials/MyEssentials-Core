package myessentials.new_config.data;

import java.lang.reflect.Field;

public class ConfigPropertyData {
    private final Field field;
    private Object defaultValue;
    private final String name;
    private final String fullName;
    private final String comment;
    private final boolean editByCommands;

    public ConfigPropertyData(Field field, String name, String fullName, String comment, boolean editByCommands) {
        this.field = field;
        try {
            defaultValue = this.field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            defaultValue = null;
        }
        this.name = name;
        this.fullName = fullName;
        this.comment = comment;
        this.editByCommands = editByCommands;
    }

    public String name() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String comment() {
        return comment;
    }

    public boolean canEditFromCommand() {
        return this.editByCommands;
    }

    public Object getValue() {
        try {
            field.setAccessible(true);
            return field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setValue(Object value) {
        Class<?> type = field.getType();
        if (!type.isAssignableFrom(value.getClass())) {
            if (value instanceof String) {
                value = parseStringValue((String) value);
            } else {
                // TODO What could we do here?
                return false;
            }
        }

        try {
            field.setAccessible(true);
            field.set(null, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Class<?> getType() {
        return field.getType();
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    private Object parseStringValue(String value) {
        Class<?> type = field.getType();

        if (Boolean.class.equals(type)) {
            return Boolean.parseBoolean(value);
        } else if (Number.class.equals(type)) {
            if (float.class.equals(type) || Float.class.equals(type)) {
                return Float.parseFloat(value);
            } else if (byte.class.equals(type) || Byte.class.equals(type)) {
                return Byte.parseByte(value);
            } else if (double.class.equals(type) || Double.class.equals(type)) {
                return Double.parseDouble(value);
            } else if (int.class.equals(type) || Integer.class.equals(type)) {
                return Integer.parseInt(value);
            } else if (long.class.equals(type) || Long.class.equals(type)) {
                return Long.parseLong(value);
            } else if (short.class.equals(type) || Short.class.equals(type)) {
                return Short.parseShort(value);
            }
        } else if (String.class.equals(type)) {
            return value;
        } else if (Character.class.equals(type)) {
            return value.charAt(0);
        }

        return null;
    }
}
