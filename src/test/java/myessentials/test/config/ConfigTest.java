package myessentials.test.config;

import myessentials.new_config.ConfigProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

public class ConfigTest {
    private int[] realIntTestArrValues = {1, 2, 3};
    private double[] realDoubleTestArrValues = {1.1, 2.2, 3.3};
    private float[] realFloatTestArrValues = {1.1f, 2.2f, 3.3f};
    private boolean[] realBoolTestArrValues = {true, false, false, true, true};
    private String[] realStringTestArrValues = {"Hello", "World!"};

//    @Before
    public void setup() {
        try {
            ConfigProcessor.load(TestConfig.class, new File(ConfigTest.class.getClassLoader().getResource("test_config.json").toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void testValues() {
        Assert.assertEquals(TestConfig.TypeTestGroup.intTest, 50);
        Assert.assertEquals(TestConfig.TypeTestGroup.doubleTest, 12.5, 0);
        Assert.assertEquals(TestConfig.TypeTestGroup.floatTest, 88.8885f, 0f);
        Assert.assertEquals(TestConfig.TypeTestGroup.booleanTest, false);
        Assert.assertEquals(TestConfig.TypeTestGroup.stringTest, "Hello World!");

        Assert.assertArrayEquals(TestConfig.ArrayTypeTestGroup.intTest, realIntTestArrValues);
        Assert.assertArrayEquals(TestConfig.ArrayTypeTestGroup.doubleTest, realDoubleTestArrValues, 0);
        Assert.assertArrayEquals(TestConfig.ArrayTypeTestGroup.floatTest, realFloatTestArrValues, 0);
        Assert.assertEquals(TestConfig.ArrayTypeTestGroup.booleanTest, realBoolTestArrValues);
        Assert.assertArrayEquals(TestConfig.ArrayTypeTestGroup.stringTest, realStringTestArrValues);

        Assert.assertEquals(TestConfig.DefaultTestGroup.aDefault, "Hello World!");
        Assert.assertEquals(TestConfig.DefaultTestGroup.anOverriddenDefault, "Yo!");
    }
}
