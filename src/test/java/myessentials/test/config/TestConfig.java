package myessentials.test.config;

import myessentials.new_config.Config;
import myessentials.new_config.data.ConfigData;

@Config
public class TestConfig {
    @Config.Instance
    public static ConfigData Data;

    @Config.Group(name = "TypeTest")
    public static class TypeTestGroup {
        @Config.Property
        public static int intTest;

        @Config.Property
        public static double doubleTest;

        @Config.Property
        public static float floatTest;

        @Config.Property
        public static boolean booleanTest;

        @Config.Property
        public static String stringTest;
    }

    @Config.Group(name = "ArrayTypeTest")
    public static class ArrayTypeTestGroup {
        @Config.Property
        public static int[] intTest;

        @Config.Property
        public static double[] doubleTest;

        @Config.Property
        public static float[] floatTest;

        @Config.Property
        public static boolean[] booleanTest;

        @Config.Property
        public static String[] stringTest;
    }

    @Config.Group(name = "DefaultGroupTest")
    public static class DefaultTestGroup {
        @Config.Property(name = "is_a_default")
        public static String aDefault = "Hello World!";

        @Config.Property(name = "is_overridden")
        public static String anOverriddenDefault = "Hello World!";
    }
}
