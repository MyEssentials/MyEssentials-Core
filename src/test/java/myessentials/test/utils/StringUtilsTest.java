package myessentials.test.utils;

import myessentials.utils.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void shouldParseInt() {

        Assert.assertTrue(StringUtils.tryParseInt("1230045"));
        Assert.assertFalse(StringUtils.tryParseInt("123.0045"));
        Assert.assertFalse(StringUtils.tryParseInt("yyyy712u"));

    }

    @Test
    public void shouldParseFloat() {

        Assert.assertTrue(StringUtils.tryParseFloat("12.55"));
        Assert.assertFalse(StringUtils.tryParseFloat("12, 55"));
        Assert.assertFalse(StringUtils.tryParseFloat("weruu34"));

    }

    @Test
    public void shouldParseBoolean() {

        Assert.assertTrue(StringUtils.tryParseBoolean("false"));
        Assert.assertFalse(StringUtils.tryParseBoolean("ggnore"));
        Assert.assertFalse(StringUtils.tryParseBoolean("00001001011110"));

    }
}
